package tw.com.ecpay.paymentgatewaykit.example.main.presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tw.com.ecpay.paymentgatewaykit.example.R;
import tw.com.ecpay.paymentgatewaykit.example.main.model.GatewaySDKModel;
import tw.com.ecpay.paymentgatewaykit.example.main.view.MainActivity;
import tw.com.ecpay.paymentgatewaykit.example.main.view.fragment.GatewaySDKFragment;
import tw.com.ecpay.paymentgatewaykit.example.util.AlertUtil;
import tw.com.ecpay.paymentgatewaykit.example.util.LogUtil;
import tw.com.ecpay.paymentgatewaykit.manager.CallbackFunction;
import tw.com.ecpay.paymentgatewaykit.manager.CreatePaymentCallbackData;
import tw.com.ecpay.paymentgatewaykit.manager.LanguageCode;
import tw.com.ecpay.paymentgatewaykit.manager.PaymentType;
import tw.com.ecpay.paymentgatewaykit.manager.PaymentkitManager;
import tw.com.ecpay.paymentgatewaykit.manager.SDKConfig;
import tw.com.ecpay.paymentgatewaykit.manager.SDKConfigBuilder;
import tw.com.ecpay.paymentgatewaykit.manager.ServerType;

public class GatewaySDKPresenter {

    private MainActivity mActivity;

    private GatewaySDKFragment mFragment;

    private GatewaySDKModel mModel;

    private ExecutorService service = Executors.newSingleThreadExecutor();

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private String appStoreName = "綠界測試商店";

    public GatewaySDKPresenter(MainActivity mActivity,
                               GatewaySDKFragment mFragment,
                               GatewaySDKModel mModel) {
        this.mActivity = mActivity;
        this.mFragment = mFragment;
        this.mModel = mModel;
    }

    private void registerForActivityResult() {
        activityResultLauncher = mFragment.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                LogUtil.log("Activity ActivityResultCallback.onActivityResult(), resultCode:" + result.getResultCode());
                createPaymentResult(result.getResultCode(), result.getData());
            }
        });
    }

    public void init() {
        registerForActivityResult();
        ServerType serverType = ServerType.Stage;
        String typeStr = "stage";

        mModel.description.set(mActivity.getString(R.string.app_name) + " " + typeStr);
        mModel.sdkVersion.set(PaymentkitManager.getPaymentgatewaykitVersion());

        sdkInit(serverType);
    }

    private void sdkInit(ServerType serverType) {
        SDKConfig config = new SDKConfigBuilder()
                .setSamsungPayServiceId("88399fbce50544e79ded9d")
                .create();
        PaymentkitManager.initialize(mActivity, serverType, config);
    }

    private void createPaymentResult(int resultCode, Intent data) {
        PaymentkitManager.createPaymentResult(mActivity, resultCode, data, new CallbackFunction<CreatePaymentCallbackData>() {
            @Override
            public void callback(CreatePaymentCallbackData callbackData) {
                switch (callbackData.getCallbackStatus()) {
                    case Success:
                        if(callbackData.getRtnCode() == 1) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("PaymentType:");
                            sb.append("\r\n");
                            sb.append(getPaymentTypeName(callbackData.getPaymentType()));
                            sb.append("\r\n");
                            sb.append("PlatformID:");
                            sb.append("\r\n");
                            sb.append(callbackData.getPlatformID());
                            sb.append("\r\n");
                            sb.append("MerchantID:");
                            sb.append("\r\n");
                            sb.append(callbackData.getMerchantID());
                            sb.append("\r\n");
                            sb.append("CustomField:");
                            sb.append("\r\n");
                            sb.append(callbackData.getCustomField());
                            sb.append("\r\n");
                            sb.append("\r\n");
                            sb.append("OrderInfo.MerchantTradeNo");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getMerchantTradeNo());
                            sb.append("\r\n");
                            sb.append("OrderInfo.TradeDate");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getTradeDate());
                            sb.append("\r\n");
                            sb.append("OrderInfo.TradeNo");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getTradeNo());
                            sb.append("\r\n");
                            sb.append("OrderInfo.TradeAmt");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getTradeAmt());
                            sb.append("\r\n");
                            sb.append("OrderInfo.PaymentType");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getPaymentType());
                            sb.append("\r\n");
                            sb.append("OrderInfo.ChargeFee");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getChargeFee());
                            sb.append("\r\n");
                            sb.append("OrderInfo.ProcessFee");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getProcessFee());
                            sb.append("\r\n");
                            sb.append("OrderInfo.TradeStatus");
                            sb.append("\r\n");
                            sb.append(callbackData.getOrderInfo().getTradeStatus());

                            if(callbackData.getPaymentType() == PaymentType.CreditCard ||
                                    callbackData.getPaymentType() == PaymentType.CreditInstallment ||
                                    callbackData.getPaymentType() == PaymentType.PeriodicFixedAmount ||
                                    callbackData.getPaymentType() == PaymentType.NationalTravelCard ||
                                    callbackData.getPaymentType() == PaymentType.UnionPay ||
                                    callbackData.getPaymentType() == PaymentType.FlexibleInstallment) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("CardInfo.AuthCode");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getAuthCode());
                                sb.append("\r\n");
                                sb.append("CardInfo.Gwsr");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getGwsr());
                                sb.append("\r\n");
                                sb.append("CardInfo.ProcessDate");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getProcessDate());
                                sb.append("\r\n");
                                sb.append("CardInfo.Amount");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getAmount());
                                sb.append("\r\n");
                                sb.append("CardInfo.Eci");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getEci());
                                sb.append("\r\n");
                                sb.append("CardInfo.Card6No");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getCard6No());
                                sb.append("\r\n");
                                sb.append("CardInfo.Card4No");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getCard4No());
                            }
                            if(callbackData.getPaymentType() == PaymentType.CreditCard) {
                                sb.append("\r\n");
                                sb.append("CardInfo.RedDan");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getRedDan());
                                sb.append("\r\n");
                                sb.append("CardInfo.RedDeAmt");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getRedDeAmt());
                                sb.append("\r\n");
                                sb.append("CardInfo.RedOkAmt");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getRedOkAmt());
                                sb.append("\r\n");
                                sb.append("CardInfo.RedYet");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getRedYet());
                            }
                            if(callbackData.getPaymentType() == PaymentType.CreditInstallment ||
                                    callbackData.getPaymentType() == PaymentType.FlexibleInstallment) {
                                sb.append("\r\n");
                                sb.append("CardInfo.Stage");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getStage());
                                sb.append("\r\n");
                                sb.append("CardInfo.Stast");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getStast());
                                sb.append("\r\n");
                                sb.append("CardInfo.Staed");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getStaed());
                            }
                            if(callbackData.getPaymentType() == PaymentType.PeriodicFixedAmount) {
                                sb.append("\r\n");
                                sb.append("CardInfo.PeriodType");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getPeriodType());
                                sb.append("\r\n");
                                sb.append("CardInfo.Frequency");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getFrequency());
                                sb.append("\r\n");
                                sb.append("CardInfo.ExecTimes");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getExecTimes());
                                sb.append("\r\n");
                                sb.append("CardInfo.PeriodAmount");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getPeriodAmount());
                                sb.append("\r\n");
                                sb.append("CardInfo.TotalSuccessTimes");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getTotalSuccessTimes());
                                sb.append("\r\n");
                                sb.append("CardInfo.TotalSuccessAmount");
                                sb.append("\r\n");
                                sb.append(callbackData.getCardInfo().getTotalSuccessAmount());
                            }

                            if(callbackData.getPaymentType() == PaymentType.ATM) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("ATMInfo.BankCode");
                                sb.append("\r\n");
                                sb.append(callbackData.getAtmInfo().getBankCode());
                                sb.append("\r\n");
                                sb.append("ATMInfo.vAccount");
                                sb.append("\r\n");
                                sb.append(callbackData.getAtmInfo().getvAccount());
                                sb.append("\r\n");
                                sb.append("ATMInfo.ExpireDate");
                                sb.append("\r\n");
                                sb.append(callbackData.getAtmInfo().getExpireDate());
                            }
                            if(callbackData.getPaymentType() == PaymentType.CVS) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("CVSInfo.PaymentNo");
                                sb.append("\r\n");
                                sb.append(callbackData.getCvsInfo().getPaymentNo());
                                sb.append("\r\n");
                                sb.append("CVSInfo.ExpireDate");
                                sb.append("\r\n");
                                sb.append(callbackData.getCvsInfo().getExpireDate());
                                sb.append("\r\n");
                                sb.append("CVSInfo.PaymentURL");
                                sb.append("\r\n");
                                sb.append(callbackData.getCvsInfo().getPaymentURL());
                            }
                            if(callbackData.getPaymentType() == PaymentType.Barcode) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("BarcodeInfo.ExpireDate");
                                sb.append("\r\n");
                                sb.append(callbackData.getBarcodeInfo().getExpireDate());
                                sb.append("\r\n");
                                sb.append("BarcodeInfo.Barcode1");
                                sb.append("\r\n");
                                sb.append(callbackData.getBarcodeInfo().getBarcode1());
                                sb.append("\r\n");
                                sb.append("BarcodeInfo.Barcode2");
                                sb.append("\r\n");
                                sb.append(callbackData.getBarcodeInfo().getBarcode2());
                                sb.append("\r\n");
                                sb.append("BarcodeInfo.Barcode3");
                                sb.append("\r\n");
                                sb.append(callbackData.getBarcodeInfo().getBarcode3());
                            }
                            if (callbackData.getCoBrandingInfo() != null) {
                                sb.append("\r\n");
                                sb.append("\r\n");
                                sb.append("CoBrandingInfo Size：");
                                sb.append(callbackData.getCoBrandingInfo().size());
                                sb.append("\r\n");
                                for (int i = 0; i < callbackData.getCoBrandingInfo().size(); i++) {
                                    sb.append("CoBrandingInfo[");
                                    sb.append(i);
                                    sb.append("].CoBrandingCode");
                                    sb.append("\r\n");
                                    sb.append(callbackData.getCoBrandingInfo().get(i).CoBrandingCode);
                                    sb.append("\r\n");
                                    sb.append("CoBrandingInfo[");
                                    sb.append(i);
                                    sb.append("].Comment");
                                    sb.append("\r\n");
                                    sb.append(callbackData.getCoBrandingInfo().get(i).Comment);
                                }
                            }

                            AlertUtil.showAlertDialog(mActivity, "提醒您", sb.toString(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }, "確定");
                        } else {
                            StringBuffer sb = new StringBuffer();
                            sb.append(callbackData.getRtnCode());
                            sb.append("\r\n");
                            sb.append(callbackData.getRtnMsg());

                            AlertUtil.showAlertDialog(mActivity, "提醒您", sb.toString(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }, "確定");
                        }
                        break;
                    case Fail:
                        AlertUtil.showAlertDialog(mActivity, "提醒您", "Fail Code=" + callbackData.getRtnCode() + ", Msg=" + callbackData.getRtnMsg(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, "確定");
                        break;
                    case Cancel:
                        AlertUtil.showAlertDialog(mActivity, "提醒您", "交易取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, "確定");
                        break;
                    case Exit:
                        AlertUtil.showAlertDialog(mActivity, "提醒您", "離開", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }, "確定");
                        break;
                }
            }
        });
    }

    private String getPaymentTypeName(PaymentType paymentType) {
        switch (paymentType) {
            case CreditCard:
                return "信用卡";
            case CreditInstallment:
                return "信用卡分期";
            case ATM:
                return "ATM虛擬帳號";
            case CVS:
                return "超商代碼";
            case Barcode:
                return "超商條碼";
            case PeriodicFixedAmount:
                return "信用卡定期定額";
            case NationalTravelCard:
                return "國旅卡";
            case UnionPay:
                return "銀聯卡";
            case FlexibleInstallment:
                return "信用卡圓夢分期";
            default:
                return "";
        }
    }

    public void onPayment() {
        callSDKCreatePayment();
    }

    public void setTitleBarBackgroundColor(){
        boolean check = PaymentkitManager.setTitleBarBackgroundColor(mActivity, mModel.titleBarBackgroundColor.get());
        AlertUtil.showAlertDialog(mActivity, "提醒您", check ? "設定成功" : "設定失敗", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }, "確定");
    }

    public void onOrientationItemSelected(int position) {
        PaymentkitManager.setOrientation(mActivity, position);
    }

    private void callSDKCreatePayment() {
        if (!TextUtils.isEmpty(mModel.token.get())) {
            LanguageCode languageCode = LanguageCode.zhTW;
            switch (mModel.languageSelectedPosition.get()) {
                case 0:
                    languageCode = LanguageCode.zhTW;
                    break;
                case 1:
                    languageCode = LanguageCode.enUS;
                    break;
            }

            boolean useResultPage = false;
            if (mModel.useResultPageSwitch.get() != null) {
                useResultPage = mModel.useResultPageSwitch.get();
            }

            String xmlMerchantID = null;
            if (mModel.xmlMerchantID.get() != null) {
                xmlMerchantID = mModel.xmlMerchantID.get();
            }

            if (TextUtils.isEmpty(xmlMerchantID)) {
                PaymentkitManager.createPayment(mActivity,
                        mModel.token.get(),
                        languageCode,
                        useResultPage,
                        appStoreName,
                        activityResultLauncher);
            } else {
                PaymentkitManager.createPayment(mActivity,
                        mModel.token.get(),
                        xmlMerchantID,
                        languageCode,
                        useResultPage,
                        appStoreName,
                        activityResultLauncher);
            }
        } else {
            AlertUtil.showAlertDialog(mActivity, "提醒您", "請輸入Token", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }, "確定");
        }
    }
}

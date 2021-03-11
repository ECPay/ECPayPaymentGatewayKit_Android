package tw.com.ecpay.paymentgatewaykit.example.gateway.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import tw.com.ecpay.paymentgatewaykit.example.R;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.ATMInfo;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.BarcodeInfo;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.CVSInfo;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.CardInfo;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.ConsumerInfo;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.DecData;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.GetTokenByTradeData;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.GetTokenByUserData;
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.OrderInfo;
import tw.com.ecpay.paymentgatewaykit.example.gateway.fragment.GatewaySDKFragment;
import tw.com.ecpay.paymentgatewaykit.example.gateway.model.GatewaySDKModel;
import tw.com.ecpay.paymentgatewaykit.example.gateway.model.ExampleData;
import tw.com.ecpay.paymentgatewaykit.example.util.Utility;
import tw.com.ecpay.paymentgatewaykit.manager.CallbackFunction;
import tw.com.ecpay.paymentgatewaykit.manager.CallbackStatus;
import tw.com.ecpay.paymentgatewaykit.manager.CreatePaymentCallbackData;
import tw.com.ecpay.paymentgatewaykit.manager.GetTokenByUserInfo;
import tw.com.ecpay.paymentgatewaykit.manager.GetTokenByUserInfoCallbackData;
import tw.com.ecpay.paymentgatewaykit.manager.ServerType;
import tw.com.ecpay.paymentgatewaykit.manager.GetTokenByTradeInfoCallbackData;
import tw.com.ecpay.paymentgatewaykit.manager.GetTokenByTradeInfo;
import tw.com.ecpay.paymentgatewaykit.manager.LanguageCode;
import tw.com.ecpay.paymentgatewaykit.manager.PaymentType;
import tw.com.ecpay.paymentgatewaykit.manager.PaymentkitManager;
import tw.com.ecpay.paymentgatewaykit.example.util.UIUtil;

public class GatewaySDKPresenter {

    private Activity mActivity;

    private GatewaySDKFragment mFragment;

    private GatewaySDKModel mModel;

    private ExampleData mExampleData;

    private ExecutorService service = Executors.newSingleThreadExecutor();

    public GatewaySDKPresenter(Activity mActivity,
                               GatewaySDKFragment mFragment,
                               GatewaySDKModel mModel,
                               ExampleData mExampleData) {
        this.mActivity = mActivity;
        this.mFragment = mFragment;
        this.mModel = mModel;
        this.mExampleData = mExampleData;
    }

    public void init() {
        ServerType serverType = ServerType.Stage;
        String typeStr = "stage";

        updateExampleData();

        mModel.description.set(mActivity.getString(R.string.app_name) + " " + typeStr);
        mModel.sdkVersion.set(PaymentkitManager.getPaymentgatewaykitVersion());

        sdkInit(serverType);
    }

    private void sdkInit(ServerType serverType) {
        PaymentkitManager.initialize(mActivity, serverType);
    }

    private void updateExampleData() {
        if(mModel.threeDSwitch.get()!=null &&
                mModel.threeDSwitch.get()) {
            mExampleData.setMerchantID("3002607");
            mExampleData.setAesKey("pwFHCqoQZGmho4w6");
            mExampleData.setAesIv("EkRm7iFT261dpevs");
        } else {
            mExampleData.setMerchantID("2000132");
            mExampleData.setAesKey("5294y06JbISpM5x9");
            mExampleData.setAesIv("v77hoKGq4kWxNNIS");
        }
    }

    public void onThreeDSwitch() {
        updateExampleData();
    }

    public void onGooglePay() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utility.log("onActivityResult(), requestCode:" + requestCode + ", resultCode:" + resultCode);
        if(requestCode == PaymentkitManager.RequestCode_CreatePayment) {
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
                                sb.append("OrderInfo.TradeStatus");
                                sb.append("\r\n");
                                sb.append(callbackData.getOrderInfo().getTradeStatus());

                                if(callbackData.getPaymentType() == PaymentType.CreditCard ||
                                        callbackData.getPaymentType() == PaymentType.CreditInstallment ||
                                        callbackData.getPaymentType() == PaymentType.PeriodicFixedAmount ||
                                        callbackData.getPaymentType() == PaymentType.NationalTravelCard) {
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
                                if(callbackData.getPaymentType() == PaymentType.CreditInstallment) {
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

                                UIUtil.showAlertDialog(mActivity, "提醒您", sb.toString(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }, "確定");
                            } else {
                                StringBuffer sb = new StringBuffer();
                                sb.append(callbackData.getRtnCode());
                                sb.append("\r\n");
                                sb.append(callbackData.getRtnMsg());

                                UIUtil.showAlertDialog(mActivity, "提醒您", sb.toString(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }, "確定");
                            }
                            break;
                        case Fail:
                            UIUtil.showAlertDialog(mActivity, "提醒您", "Fail Code=" + callbackData.getRtnCode() + ", Msg=" + callbackData.getRtnMsg(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }, "確定");
                            break;
                        case Cancel:
                            UIUtil.showAlertDialog(mActivity, "提醒您", "交易取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }, "確定");
                            break;
                    }
                }
            });
        } else if(requestCode == PaymentkitManager.RequestCode_GooglePay) {
            PaymentkitManager.googlePayResult(mActivity, resultCode, data);
        }
    }

    public void onSamsungPay() {
        PaymentkitManager.onSamsungPay(mActivity, mFragment,
                PaymentkitManager.RequestCode_SamsungPay);
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
            default:
                return "";
        }
    }

    /**
     * 模擬取得Token.
     */
    public void onSdkGetToken() {
        switch (mModel.getTokenType.get()) {
            case R.id.getTokenType1:
                getTokenByTrade(2);
                break;
            case R.id.getTokenType2:
                getTokenByTrade(1);
                break;
            case R.id.getTokenType3:
                getTokenByTrade(0);
                break;
            case R.id.getTokenType4:
                getTokenByUser();
                break;
        }
    }

    public void getTokenByTrade(int paymentUIType) {
        final ProgressDialog progressDialog = UIUtil.createProgressDialog(mActivity);
        progressDialog.show();
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    CallbackFunction<GetTokenByTradeInfoCallbackData> callback = new CallbackFunction<GetTokenByTradeInfoCallbackData>() {
                        @Override
                        public void callback(GetTokenByTradeInfoCallbackData callbackData) {
                            try {
                                if(callbackData.getCallbackStatus() ==
                                        CallbackStatus.Success) {
                                    if(callbackData.getRtnCode() == 1) {
                                        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                                        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(mExampleData.getAesKey().getBytes(),
                                                "AES"), new IvParameterSpec(mExampleData.getAesIv().getBytes()));
                                        byte[] decBytes = cipher.doFinal(Base64.decode(callbackData.getData(), Base64.NO_WRAP));
                                        String resJson = URLDecoder.decode(new String(decBytes));
                                        Utility.log("resJson " + resJson);
                                        DecData decData = new Gson().fromJson(resJson, DecData.class);
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkApiGetToken(decData);
                                            }
                                        });
                                    } else {
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                UIUtil.showAlertDialog(mActivity, "提醒您", callbackData.getRtnMsg(), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }, "確定");
                                            }
                                        });
                                    }
                                }
                            } catch (Exception ex) {
                                Utility.exceptionLog(ex);
                            }
                        }
                    };
                    callApiGetTokenByTrade(paymentUIType, callback);
                } catch (Exception ex) {
                    Utility.exceptionLog(ex);
                }
                progressDialog.dismiss();
            }
        });
    }

    public void getTokenByUser() {
        final ProgressDialog progressDialog = UIUtil.createProgressDialog(mActivity);
        progressDialog.show();
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    CallbackFunction<GetTokenByUserInfoCallbackData> callback = new CallbackFunction<GetTokenByUserInfoCallbackData>() {
                        @Override
                        public void callback(GetTokenByUserInfoCallbackData callbackData) {
                            try {
                                if(callbackData.getCallbackStatus() ==
                                        CallbackStatus.Success) {
                                    if(callbackData.getRtnCode() == 1) {
                                        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                                        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(mExampleData.getAesKey().getBytes(),
                                                "AES"), new IvParameterSpec(mExampleData.getAesIv().getBytes()));
                                        byte[] decBytes = cipher.doFinal(Base64.decode(callbackData.getData(), Base64.NO_WRAP));
                                        String resJson = URLDecoder.decode(new String(decBytes));
                                        Utility.log("resJson " + resJson);
                                        DecData decData = new Gson().fromJson(resJson, DecData.class);
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkApiGetToken(decData);
                                            }
                                        });
                                    } else {
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                UIUtil.showAlertDialog(mActivity, "提醒您", callbackData.getRtnMsg(), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }, "確定");
                                            }
                                        });
                                    }
                                }
                            } catch (Exception ex) {
                                Utility.exceptionLog(ex);
                            }
                        }
                    };
                    callApiGetTokenByUser(callback);
                } catch (Exception ex) {
                    Utility.exceptionLog(ex);
                }
                progressDialog.dismiss();
            }
        });
    }

    private void checkApiGetToken(DecData decData) {
        if(decData.RtnCode == 1) {
            mModel.token.set(decData.Token);
        } else {
            UIUtil.showAlertDialog(mActivity, "提醒您", decData.RtnMsg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }, "確定");
            mModel.token.set("");
        }
    }

    private void callApiGetTokenByTrade(int paymentUIType,
                                        CallbackFunction<GetTokenByTradeInfoCallbackData> callback) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        // 交易金額
        int totalAmount = 200;

        OrderInfo orderInfo = new OrderInfo(
                dateFormat.format(System.currentTimeMillis()),
                String.valueOf(System.currentTimeMillis()),
                totalAmount,
                "https://www.ecpay.com.tw/" ,
                "",
                "");

        CardInfo cardInfo = null;
        if(paymentUIType == 0) {
            // 信用卡定期定額
            cardInfo = new CardInfo(
                    "https://www.ecpay.com.tw/",
                    totalAmount,
                    "M",
                    3,
                    5,
                    "https://www.ecpay.com.tw/");
        } else if(paymentUIType == 1) {
            // 國旅卡
            cardInfo = new CardInfo(
                    "0",
                    "https://www.ecpay.com.tw/",
                    "01012020",
                    "01012029",
                    "001");
        } else if(paymentUIType == 2) {
            // 付款選擇清單頁
            cardInfo = new CardInfo(
                    "0",
                    "https://www.ecpay.com.tw/",
                    "3,6");
        }

        ATMInfo atmInfo = new ATMInfo(
                5);

        CVSInfo cvsInfo = new CVSInfo(
                10080,
                "條碼一",
                "條碼二",
                "條碼三",
                "條碼四");

        BarcodeInfo barcodeInfo = new BarcodeInfo(
                5);

        ConsumerInfo consumerInfo = new ConsumerInfo(
                mExampleData.getMerchantMemberID(),
                mExampleData.getEmail(),
                mExampleData.getPhone(),
                mExampleData.getName(),
                mExampleData.getCountryCode(),
                mExampleData.getAddress());

        GetTokenByTradeData getTokenByTradeData = new GetTokenByTradeData(
                null,
                mExampleData.getMerchantID(),
                1,
                paymentUIType,
                "0",
                orderInfo,
                cardInfo,
                atmInfo,
                cvsInfo,
                barcodeInfo,
                consumerInfo);

        String data = new Gson().toJson(getTokenByTradeData);

        Utility.log(data);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(mExampleData.getAesKey().getBytes(),
                "AES"), new IvParameterSpec(mExampleData.getAesIv().getBytes()));
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        String base64Data = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);

        GetTokenByTradeInfo getTokenByTradeInfo = new GetTokenByTradeInfo();
        getTokenByTradeInfo.setRqID(String.valueOf(System.currentTimeMillis()));
        getTokenByTradeInfo.setRevision(mExampleData.getRevision());
        getTokenByTradeInfo.setMerchantID(mExampleData.getMerchantID());
        getTokenByTradeInfo.setData(base64Data);

        PaymentkitManager.testGetTokenByTrade(mActivity,
                getTokenByTradeInfo, callback);
    }

    private void callApiGetTokenByUser(CallbackFunction<GetTokenByUserInfoCallbackData> callback) throws Exception {

        ConsumerInfo consumerInfo = new ConsumerInfo(
                mExampleData.getMerchantMemberID(),
                mExampleData.getEmail(),
                mExampleData.getPhone(),
                mExampleData.getName(),
                mExampleData.getCountryCode(),
                mExampleData.getAddress());

        GetTokenByUserData getTokenByUserData = new GetTokenByUserData(
                null,
                mExampleData.getMerchantID(),
                consumerInfo);

        String data = new Gson().toJson(getTokenByUserData);

        Utility.log(data);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(mExampleData.getAesKey().getBytes(),
                "AES"), new IvParameterSpec(mExampleData.getAesIv().getBytes()));
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        String base64Data = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);

        GetTokenByUserInfo getTokenByUserInfo = new GetTokenByUserInfo();
        getTokenByUserInfo.setRqID(String.valueOf(System.currentTimeMillis()));
        getTokenByUserInfo.setRevision(mExampleData.getRevision());
        getTokenByUserInfo.setMerchantID(mExampleData.getMerchantID());
        getTokenByUserInfo.setData(base64Data);

        PaymentkitManager.testGetTokenByUser(mActivity,
                getTokenByUserInfo, callback);
    }

    public void onPayment() {
        if(!TextUtils.isEmpty(mModel.token.get())) {

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
            if(mModel.useResultPageSwitch.get()!=null) {
                useResultPage = mModel.useResultPageSwitch.get();
            }

            String xmlMerchantID = null;
            if(mModel.xmlMerchantID.get()!=null) {
                xmlMerchantID = mModel.xmlMerchantID.get();
            }

            if(TextUtils.isEmpty(xmlMerchantID)) {
                PaymentkitManager.createPayment(mActivity, mFragment,
                        mModel.token.get(), languageCode, useResultPage,
                        mExampleData.getAppStoreName(), PaymentkitManager.RequestCode_CreatePayment);
            } else {
                PaymentkitManager.createPayment(mActivity, mFragment,
                        mModel.token.get(), xmlMerchantID, languageCode, useResultPage,
                        mExampleData.getAppStoreName(), PaymentkitManager.RequestCode_CreatePayment);
            }

        } else {
            UIUtil.showAlertDialog(mActivity, "提醒您", "請輸入Token", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }, "確定");
        }
    }

}

package tw.com.ecpay.paymentgatewaykit.example.main.presenter

import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import tw.com.ecpay.paymentgatewaykit.example.R
import tw.com.ecpay.paymentgatewaykit.example.main.model.GatewaySDKModel
import tw.com.ecpay.paymentgatewaykit.example.main.view.MainActivity
import tw.com.ecpay.paymentgatewaykit.example.main.view.fragment.GatewaySDKFragment
import tw.com.ecpay.paymentgatewaykit.example.util.AlertUtil
import tw.com.ecpay.paymentgatewaykit.example.util.LogUtil
import tw.com.ecpay.paymentgatewaykit.manager.CallbackFunction
import tw.com.ecpay.paymentgatewaykit.manager.CallbackStatus
import tw.com.ecpay.paymentgatewaykit.manager.LanguageCode
import tw.com.ecpay.paymentgatewaykit.manager.PaymentType
import tw.com.ecpay.paymentgatewaykit.manager.PaymentkitManager
import tw.com.ecpay.paymentgatewaykit.manager.ServerType

class GatewaySDKPresenter(
    private val mActivity: MainActivity,
    private val mFragment: GatewaySDKFragment,
    private val mModel: GatewaySDKModel,
) {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val appStoreName = "綠界測試商店"

    private fun registerForActivityResult() {
        activityResultLauncher =
            mFragment.registerForActivityResult(StartActivityForResult(),
                ActivityResultCallback<ActivityResult> { result ->
                    LogUtil.log("Activity ActivityResultCallback.onActivityResult(), resultCode:" + result.resultCode)
                    createPaymentResult(result.resultCode, result.data)
                }
            )
    }

    fun init() {
        registerForActivityResult()
        val serverType = ServerType.Stage
        val typeStr = "stage"

        mModel.description.set(
            "${mActivity.getString(R.string.app_name)} $typeStr"
        )
        mModel.sdkVersion.set(PaymentkitManager.getPaymentgatewaykitVersion())

        sdkInit(serverType)
    }

    private fun sdkInit(serverType: ServerType) {
        PaymentkitManager.initialize(mActivity, serverType)
    }

    fun createPaymentResult(resultCode: Int, data: Intent?) {
        PaymentkitManager.createPaymentResult(
            mActivity,
            resultCode,
            data,
            CallbackFunction { callbackData ->
                when (callbackData.callbackStatus) {
                    CallbackStatus.Success -> if (callbackData.rtnCode == 1) {
                        val sb = StringBuffer()
                        sb.append("PaymentType:")
                        sb.append("\r\n")
                        sb.append(getPaymentTypeName(callbackData.paymentType))
                        sb.append("\r\n")
                        sb.append("PlatformID:")
                        sb.append("\r\n")
                        sb.append(callbackData.platformID)
                        sb.append("\r\n")
                        sb.append("MerchantID:")
                        sb.append("\r\n")
                        sb.append(callbackData.merchantID)
                        sb.append("\r\n")
                        sb.append("CustomField:")
                        sb.append("\r\n")
                        sb.append(callbackData.customField)
                        sb.append("\r\n")
                        sb.append("\r\n")
                        sb.append("OrderInfo.MerchantTradeNo")
                        sb.append("\r\n")
                        sb.append(callbackData.orderInfo.merchantTradeNo)
                        sb.append("\r\n")
                        sb.append("OrderInfo.TradeDate")
                        sb.append("\r\n")
                        sb.append(callbackData.orderInfo.tradeDate)
                        sb.append("\r\n")
                        sb.append("OrderInfo.TradeNo")
                        sb.append("\r\n")
                        sb.append(callbackData.orderInfo.tradeNo)
                        sb.append("\r\n")
                        sb.append("OrderInfo.TradeAmt")
                        sb.append("\r\n")
                        sb.append(callbackData.orderInfo.tradeAmt)
                        sb.append("\r\n")
                        sb.append("OrderInfo.PaymentType")
                        sb.append("\r\n")
                        sb.append(callbackData.orderInfo.paymentType)
                        sb.append("\r\n")
                        sb.append("OrderInfo.ChargeFee")
                        sb.append("\r\n")
                        sb.append(callbackData.orderInfo.chargeFee)
                        sb.append("\r\n")
                        sb.append("OrderInfo.ProcessFee")
                        sb.append("\r\n")
                        sb.append(callbackData.orderInfo.processFee)
                        sb.append("\r\n")
                        sb.append("OrderInfo.TradeStatus")
                        sb.append("\r\n")
                        sb.append(callbackData.orderInfo.tradeStatus)

                        if (callbackData.paymentType in arrayOf(
                                PaymentType.CreditCard,
                                PaymentType.CreditInstallment,
                                PaymentType.PeriodicFixedAmount,
                                PaymentType.NationalTravelCard,
                                PaymentType.UnionPay,
                                PaymentType.FlexibleInstallment
                            )
                        ) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("CardInfo.AuthCode")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.authCode)
                            sb.append("\r\n")
                            sb.append("CardInfo.Gwsr")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.gwsr)
                            sb.append("\r\n")
                            sb.append("CardInfo.ProcessDate")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.processDate)
                            sb.append("\r\n")
                            sb.append("CardInfo.Amount")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.amount)
                            sb.append("\r\n")
                            sb.append("CardInfo.Eci")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.eci)
                            sb.append("\r\n")
                            sb.append("CardInfo.Card6No")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.card6No)
                            sb.append("\r\n")
                            sb.append("CardInfo.Card4No")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.card4No)
                        }
                        if (callbackData.paymentType == PaymentType.CreditCard) {
                            sb.append("\r\n")
                            sb.append("CardInfo.RedDan")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.redDan)
                            sb.append("\r\n")
                            sb.append("CardInfo.RedDeAmt")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.redDeAmt)
                            sb.append("\r\n")
                            sb.append("CardInfo.RedOkAmt")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.redOkAmt)
                            sb.append("\r\n")
                            sb.append("CardInfo.RedYet")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.redYet)
                        }
                        if (callbackData.paymentType in arrayOf(
                                PaymentType.CreditInstallment,
                                PaymentType.FlexibleInstallment
                            )
                        ) {
                            sb.append("\r\n")
                            sb.append("CardInfo.Stage")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.stage)
                            sb.append("\r\n")
                            sb.append("CardInfo.Stast")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.stast)
                            sb.append("\r\n")
                            sb.append("CardInfo.Staed")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.staed)
                        }
                        if (callbackData.paymentType == PaymentType.PeriodicFixedAmount) {
                            sb.append("\r\n")
                            sb.append("CardInfo.PeriodType")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.periodType)
                            sb.append("\r\n")
                            sb.append("CardInfo.Frequency")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.frequency)
                            sb.append("\r\n")
                            sb.append("CardInfo.ExecTimes")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.execTimes)
                            sb.append("\r\n")
                            sb.append("CardInfo.PeriodAmount")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.periodAmount)
                            sb.append("\r\n")
                            sb.append("CardInfo.TotalSuccessTimes")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.totalSuccessTimes)
                            sb.append("\r\n")
                            sb.append("CardInfo.TotalSuccessAmount")
                            sb.append("\r\n")
                            sb.append(callbackData.cardInfo.totalSuccessAmount)
                        }

                        if (callbackData.paymentType == PaymentType.ATM) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("ATMInfo.BankCode")
                            sb.append("\r\n")
                            sb.append(callbackData.atmInfo.bankCode)
                            sb.append("\r\n")
                            sb.append("ATMInfo.vAccount")
                            sb.append("\r\n")
                            sb.append(callbackData.atmInfo.vAccount)
                            sb.append("\r\n")
                            sb.append("ATMInfo.ExpireDate")
                            sb.append("\r\n")
                            sb.append(callbackData.atmInfo.expireDate)
                        }
                        if (callbackData.paymentType == PaymentType.CVS) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("CVSInfo.PaymentNo")
                            sb.append("\r\n")
                            sb.append(callbackData.cvsInfo.paymentNo)
                            sb.append("\r\n")
                            sb.append("CVSInfo.ExpireDate")
                            sb.append("\r\n")
                            sb.append(callbackData.cvsInfo.expireDate)
                            sb.append("\r\n")
                            sb.append("CVSInfo.PaymentURL")
                            sb.append("\r\n")
                            sb.append(callbackData.cvsInfo.paymentURL)
                        }
                        if (callbackData.paymentType == PaymentType.Barcode) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("BarcodeInfo.ExpireDate")
                            sb.append("\r\n")
                            sb.append(callbackData.barcodeInfo.expireDate)
                            sb.append("\r\n")
                            sb.append("BarcodeInfo.Barcode1")
                            sb.append("\r\n")
                            sb.append(callbackData.barcodeInfo.barcode1)
                            sb.append("\r\n")
                            sb.append("BarcodeInfo.Barcode2")
                            sb.append("\r\n")
                            sb.append(callbackData.barcodeInfo.barcode2)
                            sb.append("\r\n")
                            sb.append("BarcodeInfo.Barcode3")
                            sb.append("\r\n")
                            sb.append(callbackData.barcodeInfo.barcode3)
                        }
                        if (callbackData.coBrandingInfo != null) {
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("CoBrandingInfo Size：")
                            sb.append(callbackData.coBrandingInfo.size)
                            sb.append("\r\n")
                            for (i in 0 until callbackData.coBrandingInfo.size) {
                                sb.append("CoBrandingInfo[")
                                sb.append(i)
                                sb.append("].CoBrandingCode")
                                sb.append("\r\n")
                                sb.append(callbackData.coBrandingInfo[i].CoBrandingCode)
                                sb.append("\r\n")
                                sb.append("CoBrandingInfo[")
                                sb.append(i)
                                sb.append("].Comment")
                                sb.append("\r\n")
                                sb.append(callbackData.coBrandingInfo[i].Comment)
                            }
                        }

                        AlertUtil.showAlertDialog(
                            mActivity,
                            "提醒您",
                            sb.toString(),
                            DialogInterface.OnClickListener { dialog, which -> },
                            "確定"
                        )
                    } else {
                        val sb = StringBuffer()
                        sb.append(callbackData.rtnCode)
                        sb.append("\r\n")
                        sb.append(callbackData.rtnMsg)
                        AlertUtil.showAlertDialog(
                            mActivity,
                            "提醒您",
                            sb.toString(),
                            DialogInterface.OnClickListener { dialog, which -> },
                            "確定"
                        )
                    }
                    CallbackStatus.Fail -> {
                        AlertUtil.showAlertDialog(
                            mActivity,
                            "提醒您",
                            "Fail Code=" + callbackData.rtnCode +
                                    ", Msg=" + callbackData.rtnMsg,
                            DialogInterface.OnClickListener { dialog, which -> },
                            "確定"
                        )
                    }
                    CallbackStatus.Cancel -> AlertUtil.showAlertDialog(
                        mActivity,
                        "提醒您",
                        "交易取消",
                        DialogInterface.OnClickListener { dialog, which -> },
                        "確定"
                    )
                    CallbackStatus.Exit -> AlertUtil.showAlertDialog(
                        mActivity,
                        "提醒您",
                        "離開",
                        DialogInterface.OnClickListener { dialog, which -> },
                        "確定"
                    )
                    else -> {
                    }
                }
            })
    }

    fun getPaymentTypeName(paymentType: PaymentType): String {
        return when (paymentType) {
            PaymentType.CreditCard -> "信用卡"
            PaymentType.CreditInstallment -> "信用卡分期"
            PaymentType.ATM -> "ATM虛擬帳號"
            PaymentType.CVS -> "超商代碼"
            PaymentType.Barcode -> "超商條碼"
            PaymentType.PeriodicFixedAmount -> "信用卡定期定額"
            PaymentType.NationalTravelCard -> "國旅卡"
            PaymentType.UnionPay -> "銀聯卡"
            PaymentType.FlexibleInstallment -> "信用卡圓夢分期"
            else -> ""
        }
    }

    fun onPayment() {
        callSDKCreatePayment()
    }

    fun setTitleBarBackgroundColor() {
        val check = PaymentkitManager.setTitleBarBackgroundColor(
            mActivity,
            mModel.titleBarBackgroundColor.get()
        )
        AlertUtil.showAlertDialog(mActivity, "提醒您", if (check) "設定成功" else "設定失敗",
            { dialog, which -> }, "確定"
        )
    }

    fun onOrientationItemSelected(position: Int) {
        PaymentkitManager.setOrientation(mActivity, position)
    }

    fun callSDKCreatePayment() {
        if (!TextUtils.isEmpty(mModel.token.get())) {
            var languageCode = LanguageCode.zhTW
            when (mModel.languageSelectedPosition.get()) {
                0 -> languageCode = LanguageCode.zhTW
                1 -> languageCode = LanguageCode.enUS
            }
            var useResultPage = false
            if (mModel.useResultPageSwitch.get() != null) {
                useResultPage = mModel.useResultPageSwitch.get()!!
            }
            var xmlMerchantID: String? = null
            if (mModel.xmlMerchantID.get() != null) {
                xmlMerchantID = mModel.xmlMerchantID.get()
            }
            if (TextUtils.isEmpty(xmlMerchantID)) {
                PaymentkitManager.createPayment(
                    mActivity,
                    mModel.token.get() ?: "",
                    languageCode,
                    useResultPage,
                    appStoreName,
                    activityResultLauncher
                )
            } else {
                PaymentkitManager.createPayment(
                    mActivity,
                    mModel.token.get() ?: "",
                    xmlMerchantID ?: "",
                    languageCode,
                    useResultPage,
                    appStoreName,
                    activityResultLauncher
                )
            }
        } else {
            AlertUtil.showAlertDialog(
                mActivity,
                "提醒您",
                "請輸入Token",
                DialogInterface.OnClickListener { dialog, which -> },
                "確定"
            )
        }
    }
}
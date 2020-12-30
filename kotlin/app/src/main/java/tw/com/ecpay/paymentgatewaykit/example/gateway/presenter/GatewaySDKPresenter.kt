package tw.com.ecpay.paymentgatewaykit.example.gateway.presenter

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.util.Base64
import com.google.gson.Gson
import java.net.URLDecoder
import java.text.SimpleDateFormat
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tw.com.ecpay.paymentgatewaykit.example.R
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.ATMInfo
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.BarcodeInfo
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.CVSInfo
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.CardInfo
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.ConsumerInfo
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.DecData
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.GetTokenByTradeData
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.GetTokenByUserData
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.OrderInfo
import tw.com.ecpay.paymentgatewaykit.example.gateway.fragment.GatewaySDKFragment
import tw.com.ecpay.paymentgatewaykit.example.gateway.model.ExampleData
import tw.com.ecpay.paymentgatewaykit.example.gateway.model.GatewaySDKModel
import tw.com.ecpay.paymentgatewaykit.example.util.UIUtil
import tw.com.ecpay.paymentgatewaykit.example.util.Utility
import tw.com.ecpay.paymentgatewaykit.manager.CallbackFunction
import tw.com.ecpay.paymentgatewaykit.manager.CallbackStatus
import tw.com.ecpay.paymentgatewaykit.manager.GetTokenByTradeInfo
import tw.com.ecpay.paymentgatewaykit.manager.GetTokenByTradeInfoCallbackData
import tw.com.ecpay.paymentgatewaykit.manager.GetTokenByUserInfo
import tw.com.ecpay.paymentgatewaykit.manager.GetTokenByUserInfoCallbackData
import tw.com.ecpay.paymentgatewaykit.manager.LanguageCode
import tw.com.ecpay.paymentgatewaykit.manager.PaymentType
import tw.com.ecpay.paymentgatewaykit.manager.PaymentkitManager
import tw.com.ecpay.paymentgatewaykit.manager.ServerType

class GatewaySDKPresenter {

    private var mActivity: Activity

    private var mFragment: GatewaySDKFragment

    private var mModel: GatewaySDKModel

    private var mExampleData: ExampleData

    private val coroutineScope: CoroutineScope

    constructor (
        mActivity: Activity,
        mFragment: GatewaySDKFragment,
        mModel: GatewaySDKModel,
        mExampleData: ExampleData
    ) {
        this.mActivity = mActivity
        this.mFragment = mFragment
        this.mModel = mModel
        this.mExampleData = mExampleData
        this.coroutineScope = CoroutineScope(EmptyCoroutineContext)
    }

    fun init() {
        val serverType = ServerType.Stage
        val typeStr = "stage"

        updateExampleData()

        mModel.description.set(
            mActivity!!.getString(R.string.app_name) + " " + typeStr
        )
        mModel.sdkVersion.set(PaymentkitManager.getPaymentgatewaykitVersion())

        sdkInit(serverType)
    }

    private fun sdkInit(serverType: ServerType) {
        PaymentkitManager.initialize(mActivity, serverType)
    }

    private fun updateExampleData() {
        if (mModel.threeDSwitch.get() != null &&
            mModel.threeDSwitch.get()!!
        ) {
            mExampleData.merchantID = "3002607"
            mExampleData.aesKey = "pwFHCqoQZGmho4w6"
            mExampleData.aesIv = "EkRm7iFT261dpevs"
        } else {
            mExampleData.merchantID = "2000132"
            mExampleData.aesKey = "5294y06JbISpM5x9"
            mExampleData.aesIv = "v77hoKGq4kWxNNIS"
        }
    }

    fun onThreeDSwitch() {
        updateExampleData()
    }

    fun onGooglePay() {
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Utility.log("onActivityResult(), requestCode:$requestCode, resultCode:$resultCode")
        if (requestCode === PaymentkitManager.RequestCode_CreatePayment) {
            PaymentkitManager.createPaymentResult(
                mActivity,
                resultCode,
                data,
                CallbackFunction { callbackData ->
                    when (callbackData.getCallbackStatus()) {
                        CallbackStatus.Success -> if (callbackData.getRtnCode() == 1) {
                            val sb = StringBuffer()
                            sb.append("PaymentType:")
                            sb.append("\r\n")
                            sb.append(getPaymentTypeName(callbackData.getPaymentType()))
                            sb.append("\r\n")
                            sb.append("\r\n")
                            sb.append("OrderInfo.MerchantTradeNo")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().getMerchantTradeNo())
                            sb.append("\r\n")
                            sb.append("OrderInfo.TradeDate")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().getTradeDate())
                            sb.append("\r\n")
                            sb.append("OrderInfo.TradeNo")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().getTradeNo())
                            if (callbackData.getPaymentType() == PaymentType.CreditCard ||
                                callbackData.getPaymentType() == PaymentType.CreditInstallment ||
                                callbackData.getPaymentType() == PaymentType.PeriodicFixedAmount ||
                                callbackData.getPaymentType() == PaymentType.NationalTravelCard
                            ) {
                                sb.append("\r\n")
                                sb.append("\r\n")
                                sb.append("CardInfo.AuthCode")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getAuthCode())
                                sb.append("\r\n")
                                sb.append("CardInfo.Gwsr")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getGwsr())
                                sb.append("\r\n")
                                sb.append("CardInfo.ProcessDate")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getProcessDate())
                                sb.append("\r\n")
                                sb.append("CardInfo.Amount")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getAmount())
                                sb.append("\r\n")
                                sb.append("CardInfo.Eci")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getEci())
                                sb.append("\r\n")
                                sb.append("CardInfo.Card6No")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getCard6No())
                                sb.append("\r\n")
                                sb.append("CardInfo.Card4No")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getCard4No())
                            }
                            if (callbackData.getPaymentType() == PaymentType.CreditCard) {
                                sb.append("\r\n")
                                sb.append("CardInfo.RedDan")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getRedDan())
                                sb.append("\r\n")
                                sb.append("CardInfo.RedDeAmt")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getRedDeAmt())
                                sb.append("\r\n")
                                sb.append("CardInfo.RedOkAmt")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getRedOkAmt())
                                sb.append("\r\n")
                                sb.append("CardInfo.RedYet")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getRedYet())
                            }
                            if (callbackData.getPaymentType() == PaymentType.CreditInstallment) {
                                sb.append("\r\n")
                                sb.append("CardInfo.Stage")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getStage())
                                sb.append("\r\n")
                                sb.append("CardInfo.Stast")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getStast())
                                sb.append("\r\n")
                                sb.append("CardInfo.Staed")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().getStaed())
                            }
                            if (callbackData.getPaymentType() == PaymentType.ATM) {
                                sb.append("\r\n")
                                sb.append("\r\n")
                                sb.append("ATMInfo.BankCode")
                                sb.append("\r\n")
                                sb.append(callbackData.getAtmInfo().getBankCode())
                                sb.append("\r\n")
                                sb.append("ATMInfo.vAccount")
                                sb.append("\r\n")
                                sb.append(callbackData.getAtmInfo().getvAccount())
                                sb.append("\r\n")
                                sb.append("ATMInfo.ExpireDate")
                                sb.append("\r\n")
                                sb.append(callbackData.getAtmInfo().getExpireDate())
                            }
                            if (callbackData.getPaymentType() == PaymentType.CVS) {
                                sb.append("\r\n")
                                sb.append("\r\n")
                                sb.append("CVSInfo.PaymentNo")
                                sb.append("\r\n")
                                sb.append(callbackData.getCvsInfo().getPaymentNo())
                                sb.append("\r\n")
                                sb.append("CVSInfo.ExpireDate")
                                sb.append("\r\n")
                                sb.append(callbackData.getCvsInfo().getExpireDate())
                                sb.append("\r\n")
                                sb.append("CVSInfo.PaymentURL")
                                sb.append("\r\n")
                                sb.append(callbackData.getCvsInfo().getPaymentURL())
                            }
                            if (callbackData.getPaymentType() == PaymentType.Barcode) {
                                sb.append("\r\n")
                                sb.append("\r\n")
                                sb.append("BarcodeInfo.ExpireDate")
                                sb.append("\r\n")
                                sb.append(callbackData.getBarcodeInfo().getExpireDate())
                                sb.append("\r\n")
                                sb.append("BarcodeInfo.Barcode1")
                                sb.append("\r\n")
                                sb.append(callbackData.getBarcodeInfo().getBarcode1())
                                sb.append("\r\n")
                                sb.append("BarcodeInfo.Barcode2")
                                sb.append("\r\n")
                                sb.append(callbackData.getBarcodeInfo().getBarcode2())
                                sb.append("\r\n")
                                sb.append("BarcodeInfo.Barcode3")
                                sb.append("\r\n")
                                sb.append(callbackData.getBarcodeInfo().getBarcode3())
                            }
                            UIUtil.showAlertDialog(
                                mActivity,
                                "提醒您",
                                sb.toString(),
                                DialogInterface.OnClickListener { dialog, which -> },
                                "確定"
                            )
                        } else {
                            val sb = StringBuffer()
                            sb.append(callbackData.getRtnCode())
                            sb.append("\r\n")
                            sb.append(callbackData.getRtnMsg())
                            UIUtil.showAlertDialog(
                                mActivity,
                                "提醒您",
                                sb.toString(),
                                DialogInterface.OnClickListener { dialog, which -> },
                                "確定"
                            )
                        }
                        CallbackStatus.Fail -> {
                            UIUtil.showAlertDialog(
                                mActivity,
                                "提醒您",
                                "Fail Code=" + callbackData.getRtnCode() +
                                        ", Msg=" + callbackData.getRtnMsg(),
                                DialogInterface.OnClickListener { dialog, which -> },
                                "確定"
                            )
                        }
                        CallbackStatus.Cancel -> UIUtil.showAlertDialog(
                            mActivity,
                            "提醒您",
                            "交易取消",
                            DialogInterface.OnClickListener { dialog, which -> },
                            "確定"
                        )
                    }
                })
        } else if (requestCode === PaymentkitManager.RequestCode_GooglePay) {
            PaymentkitManager.googlePayResult(mActivity, resultCode, data)
        }
    }

    fun onSamsungPay() {
        PaymentkitManager.onSamsungPay(mActivity, mFragment,
            PaymentkitManager.RequestCode_SamsungPay)
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
            else -> ""
        }
    }

    fun onSdkGetToken() {
        when (mModel.getTokenType.get()) {
            R.id.getTokenType1 -> getTokenByTrade(2)
            R.id.getTokenType2 -> getTokenByTrade(1)
            R.id.getTokenType3 -> getTokenByTrade(0)
            R.id.getTokenType4 -> getTokenByUser()
        }
    }

    fun getTokenByTrade(paymentUIType: Int) {
        val progressDialog = UIUtil.createProgressDialog(mActivity)
        progressDialog!!.show()
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val callback =
                    CallbackFunction<GetTokenByTradeInfoCallbackData> { callbackData ->
                        try {
                            if (callbackData.getCallbackStatus() ==
                                CallbackStatus.Success
                            ) {
                                if (callbackData.getRtnCode() == 1) {
                                    val cipher: Cipher =
                                        Cipher.getInstance("AES/CBC/PKCS5Padding")
                                    cipher.init(
                                        Cipher.DECRYPT_MODE, SecretKeySpec(
                                            mExampleData.aesKey.toByteArray(),
                                            "AES"
                                        ), IvParameterSpec(mExampleData.aesIv.toByteArray())
                                    )
                                    val decBytes: ByteArray = cipher.doFinal(
                                        Base64.decode(
                                            callbackData.data,
                                            Base64.NO_WRAP
                                        )
                                    )
                                    val resJson: String =
                                        URLDecoder.decode(String(decBytes))
                                    Utility.log("resJson $resJson")
                                    val decData = Gson().fromJson(
                                        resJson,
                                        DecData::class.java
                                    )
                                    coroutineScope.launch(Dispatchers.Main) {
                                        checkApiGetToken(decData)
                                    }
                                } else {
                                    coroutineScope.launch(Dispatchers.Main) {
                                        UIUtil.showAlertDialog(
                                            mActivity,
                                            "提醒您",
                                            callbackData.getRtnMsg(),
                                            DialogInterface.OnClickListener { dialog, which -> },
                                            "確定"
                                        )
                                    }
                                }
                            }
                        } catch (ex: Exception) {
                            Utility.exceptionLog(ex)
                        }
                    }
                callApiGetTokenByTrade(paymentUIType, callback)
            } catch (ex: Exception) {
                Utility.exceptionLog(ex)
            }
            progressDialog!!.dismiss()
        }
    }

    fun getTokenByUser() {
        val progressDialog = UIUtil.createProgressDialog(mActivity)
        progressDialog!!.show()
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val callback =
                    CallbackFunction<GetTokenByUserInfoCallbackData> { callbackData ->
                        try {
                            if (callbackData.getCallbackStatus() ==
                                CallbackStatus.Success
                            ) {
                                if (callbackData.getRtnCode() == 1) {
                                    val cipher =
                                        Cipher.getInstance("AES/CBC/PKCS5Padding")
                                    cipher.init(
                                        Cipher.DECRYPT_MODE,
                                        SecretKeySpec(
                                            mExampleData.aesKey.toByteArray(),
                                            "AES"
                                        ),
                                        IvParameterSpec(mExampleData.aesIv.toByteArray())
                                    )
                                    val decBytes = cipher.doFinal(
                                        Base64.decode(
                                            callbackData.data,
                                            Base64.NO_WRAP
                                        )
                                    )
                                    val resJson =
                                        URLDecoder.decode(String(decBytes))
                                    Utility.log("resJson $resJson")
                                    val decData = Gson().fromJson(
                                        resJson,
                                        DecData::class.java
                                    )
                                    coroutineScope.launch(Dispatchers.Main) {
                                        checkApiGetToken(decData)
                                    }
                                } else {
                                    coroutineScope.launch(Dispatchers.Main) {
                                        UIUtil.showAlertDialog(
                                            mActivity,
                                            "提醒您",
                                            callbackData.getRtnMsg(),
                                            DialogInterface.OnClickListener { dialog, which -> },
                                            "確定"
                                        )
                                    }
                                }
                            }
                        } catch (ex: java.lang.Exception) {
                            Utility.exceptionLog(ex)
                        }
                    }
                callApiGetTokenByUser(callback)
            } catch (ex: java.lang.Exception) {
                Utility.exceptionLog(ex)
            }
            progressDialog!!.dismiss()
        }
    }

    fun checkApiGetToken(decData: DecData) {
        if (decData.RtnCode === 1) {
            mModel.token.set(decData.Token)
        } else {
            UIUtil.showAlertDialog(
                mActivity,
                "提醒您",
                decData.RtnMsg,
                DialogInterface.OnClickListener { dialog, which -> },
                "確定"
            )
            mModel.token.set("")
        }
    }

    private fun callApiGetTokenByTrade(
        paymentUIType: Int,
        callback: CallbackFunction<GetTokenByTradeInfoCallbackData>
    ) {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

        // 交易金額

        // 交易金額
        val totalAmount = 200

        val orderInfo = OrderInfo(
            dateFormat.format(System.currentTimeMillis()),
            System.currentTimeMillis().toString(),
            totalAmount,
            "https://www.ecpay.com.tw/",
            "",
            ""
        )

        var cardInfo: CardInfo? = null
        if (paymentUIType === 0) {
            // 信用卡定期定額
            cardInfo = CardInfo(
                null,
                "https://www.ecpay.com.tw/",
                totalAmount,
                "M",
                3,
                5,
                null,
                null,
                null,
                null,
                null
            )
        } else if (paymentUIType === 1) {
            // 國旅卡
            cardInfo = CardInfo(
                "0",
                "https://www.ecpay.com.tw/",
                0,
                null,
                0,
                0,
                null,
                null,
                "01012020",
                "01012029",
                "001"
            )
        } else if (paymentUIType === 2) {
            // 付款選擇清單頁
            cardInfo = CardInfo(
                "0",
                "https://www.ecpay.com.tw/",
                0,
                null,
                0,
                0,
                null,
                "3,6",
                null,
                null,
                null
            )
        }

        val atmInfo = ATMInfo(
            5
        )

        val cvsInfo = CVSInfo(
            10080,
            "條碼一",
            "條碼二",
            "條碼三",
            "條碼四"
        )

        val barcodeInfo = BarcodeInfo(
            5
        )

        val consumerInfo = ConsumerInfo(
            mExampleData.merchantMemberID,
            mExampleData.email,
            mExampleData.phone,
            mExampleData.name,
            mExampleData.countryCode,
            mExampleData.address
        )

        val getTokenByTradeData = GetTokenByTradeData(
            null,
            mExampleData.merchantID,
            1,
            paymentUIType,
            "0",
            orderInfo,
            cardInfo,
            atmInfo,
            cvsInfo,
            barcodeInfo,
            consumerInfo
        )

        val data = Gson().toJson(getTokenByTradeData)

        Utility.log(data)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(
            Cipher.ENCRYPT_MODE, SecretKeySpec(
                mExampleData.aesKey.toByteArray(),
                "AES"
            ), IvParameterSpec(mExampleData.aesIv.toByteArray())
        )
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        val base64Data =
            Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)

        val getTokenByTradeInfo = GetTokenByTradeInfo()
        getTokenByTradeInfo.setRqID(System.currentTimeMillis().toString())
        getTokenByTradeInfo.setRevision(mExampleData.revision)
        getTokenByTradeInfo.setMerchantID(mExampleData.merchantID)
        getTokenByTradeInfo.setData(base64Data)

        PaymentkitManager.testGetTokenByTrade(
            mActivity,
            getTokenByTradeInfo, callback
        )
    }

    private fun callApiGetTokenByUser(callback: CallbackFunction<GetTokenByUserInfoCallbackData>) {
        val consumerInfo = ConsumerInfo(
            mExampleData.merchantMemberID,
            mExampleData.email,
            mExampleData.phone,
            mExampleData.name,
            mExampleData.countryCode,
            mExampleData.address
        )

        val getTokenByUserData = GetTokenByUserData(
            null,
            mExampleData.merchantID,
            consumerInfo
        )

        val data = Gson().toJson(getTokenByUserData)

        Utility.log(data)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(
            Cipher.ENCRYPT_MODE, SecretKeySpec(
                mExampleData.aesKey.toByteArray(),
                "AES"
            ), IvParameterSpec(mExampleData.aesIv.toByteArray())
        )
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        val base64Data =
            Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)

        val getTokenByUserInfo = GetTokenByUserInfo()
        getTokenByUserInfo.setRqID(System.currentTimeMillis().toString())
        getTokenByUserInfo.setRevision(mExampleData.revision)
        getTokenByUserInfo.setMerchantID(mExampleData.merchantID)
        getTokenByUserInfo.setData(base64Data)

        PaymentkitManager.testGetTokenByUser(
            mActivity,
            getTokenByUserInfo, callback
        )
    }

    fun onPayment() {
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
                    mActivity, mFragment,
                    mModel.token.get() ?: "", languageCode, useResultPage,
                    mExampleData.appStoreName, PaymentkitManager.RequestCode_CreatePayment
                )
            } else {
                PaymentkitManager.createPayment(
                    mActivity, mFragment,
                    mModel.token.get() ?: "", xmlMerchantID ?: "", languageCode, useResultPage,
                    mExampleData.appStoreName, PaymentkitManager.RequestCode_CreatePayment
                )
            }
        } else {
            UIUtil.showAlertDialog(
                mActivity,
                "提醒您",
                "請輸入Token",
                DialogInterface.OnClickListener { dialog, which -> },
                "確定"
            )
        }
    }
}
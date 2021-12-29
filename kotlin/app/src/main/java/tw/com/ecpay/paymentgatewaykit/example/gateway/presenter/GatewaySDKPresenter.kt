package tw.com.ecpay.paymentgatewaykit.example.gateway.presenter

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.util.Base64
import com.google.gson.Gson
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
import tw.com.ecpay.paymentgatewaykit.example.gateway.api.UnionPayInfo
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
import java.net.URLDecoder
import java.text.SimpleDateFormat
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.coroutines.EmptyCoroutineContext

class GatewaySDKPresenter(
    private var mActivity: Activity,
    private var mFragment: GatewaySDKFragment,
    private var mModel: GatewaySDKModel,
    private var mExampleData: ExampleData,
    private val coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
) {

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
        if (requestCode == PaymentkitManager.RequestCode_CreatePayment) {
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
                            sb.append(callbackData.getOrderInfo().merchantTradeNo)
                            sb.append("\r\n")
                            sb.append("OrderInfo.TradeDate")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().tradeDate)
                            sb.append("\r\n")
                            sb.append("OrderInfo.TradeNo")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().tradeNo)
                            sb.append("\r\n")
                            sb.append("OrderInfo.TradeAmt")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().tradeAmt)
                            sb.append("\r\n")
                            sb.append("OrderInfo.PaymentType")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().paymentType)
                            sb.append("\r\n")
                            sb.append("OrderInfo.ChargeFee")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().chargeFee)
                            sb.append("\r\n")
                            sb.append("OrderInfo.TradeStatus")
                            sb.append("\r\n")
                            sb.append(callbackData.getOrderInfo().tradeStatus)

                            if (callbackData.getPaymentType() in arrayOf(
                                    PaymentType.CreditCard,
                                    PaymentType.CreditInstallment,
                                    PaymentType.PeriodicFixedAmount,
                                    PaymentType.NationalTravelCard,
                                    PaymentType.UnionPay
                                )
                            ) {
                                sb.append("\r\n")
                                sb.append("\r\n")
                                sb.append("CardInfo.AuthCode")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().authCode)
                                sb.append("\r\n")
                                sb.append("CardInfo.Gwsr")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().gwsr)
                                sb.append("\r\n")
                                sb.append("CardInfo.ProcessDate")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().processDate)
                                sb.append("\r\n")
                                sb.append("CardInfo.Amount")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().amount)
                                sb.append("\r\n")
                                sb.append("CardInfo.Eci")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().eci)
                                sb.append("\r\n")
                                sb.append("CardInfo.Card6No")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().card6No)
                                sb.append("\r\n")
                                sb.append("CardInfo.Card4No")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().card4No)
                            }
                            if (callbackData.getPaymentType() in arrayOf(
                                    PaymentType.CreditCard,
                                    PaymentType.UnionPay
                                )
                            ) {
                                sb.append("\r\n")
                                sb.append("CardInfo.RedDan")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().redDan)
                                sb.append("\r\n")
                                sb.append("CardInfo.RedDeAmt")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().redDeAmt)
                                sb.append("\r\n")
                                sb.append("CardInfo.RedOkAmt")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().redOkAmt)
                                sb.append("\r\n")
                                sb.append("CardInfo.RedYet")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().redYet)
                            }
                            if (callbackData.getPaymentType() == PaymentType.CreditInstallment) {
                                sb.append("\r\n")
                                sb.append("CardInfo.Stage")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().stage)
                                sb.append("\r\n")
                                sb.append("CardInfo.Stast")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().stast)
                                sb.append("\r\n")
                                sb.append("CardInfo.Staed")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().staed)
                            }
                            if(callbackData.getPaymentType() == PaymentType.PeriodicFixedAmount) {
                                sb.append("\r\n")
                                sb.append("CardInfo.PeriodType")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().periodType)
                                sb.append("\r\n")
                                sb.append("CardInfo.Frequency")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().frequency)
                                sb.append("\r\n")
                                sb.append("CardInfo.ExecTimes")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().execTimes)
                                sb.append("\r\n")
                                sb.append("CardInfo.PeriodAmount")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().periodAmount)
                                sb.append("\r\n")
                                sb.append("CardInfo.TotalSuccessTimes")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().totalSuccessTimes)
                                sb.append("\r\n")
                                sb.append("CardInfo.TotalSuccessAmount")
                                sb.append("\r\n")
                                sb.append(callbackData.getCardInfo().totalSuccessAmount)
                            }

                            if (callbackData.getPaymentType() == PaymentType.ATM) {
                                sb.append("\r\n")
                                sb.append("\r\n")
                                sb.append("ATMInfo.BankCode")
                                sb.append("\r\n")
                                sb.append(callbackData.getAtmInfo().bankCode)
                                sb.append("\r\n")
                                sb.append("ATMInfo.vAccount")
                                sb.append("\r\n")
                                sb.append(callbackData.getAtmInfo().vAccount)
                                sb.append("\r\n")
                                sb.append("ATMInfo.ExpireDate")
                                sb.append("\r\n")
                                sb.append(callbackData.getAtmInfo().expireDate)
                            }
                            if (callbackData.getPaymentType() == PaymentType.CVS) {
                                sb.append("\r\n")
                                sb.append("\r\n")
                                sb.append("CVSInfo.PaymentNo")
                                sb.append("\r\n")
                                sb.append(callbackData.getCvsInfo().paymentNo)
                                sb.append("\r\n")
                                sb.append("CVSInfo.ExpireDate")
                                sb.append("\r\n")
                                sb.append(callbackData.getCvsInfo().expireDate)
                                sb.append("\r\n")
                                sb.append("CVSInfo.PaymentURL")
                                sb.append("\r\n")
                                sb.append(callbackData.getCvsInfo().paymentURL)
                            }
                            if (callbackData.getPaymentType() == PaymentType.Barcode) {
                                sb.append("\r\n")
                                sb.append("\r\n")
                                sb.append("BarcodeInfo.ExpireDate")
                                sb.append("\r\n")
                                sb.append(callbackData.getBarcodeInfo().expireDate)
                                sb.append("\r\n")
                                sb.append("BarcodeInfo.Barcode1")
                                sb.append("\r\n")
                                sb.append(callbackData.getBarcodeInfo().barcode1)
                                sb.append("\r\n")
                                sb.append("BarcodeInfo.Barcode2")
                                sb.append("\r\n")
                                sb.append(callbackData.getBarcodeInfo().barcode2)
                                sb.append("\r\n")
                                sb.append("BarcodeInfo.Barcode3")
                                sb.append("\r\n")
                                sb.append(callbackData.getBarcodeInfo().barcode3)
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
                        CallbackStatus.Exit -> UIUtil.showAlertDialog(
                            mActivity,
                            "提醒您",
                            "離開",
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
        PaymentkitManager.onSamsungPay(
            mActivity, mFragment,
            PaymentkitManager.RequestCode_SamsungPay
        )
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
        val totalAmount = 200
        // 信用卡紅利折抵
        val redeem = if (mModel.redeemSwitch.get() != null && mModel.redeemSwitch.get()!!) "1" else "0"

        val orderInfo = OrderInfo(
            dateFormat.format(System.currentTimeMillis()),
            System.currentTimeMillis().toString(),
            totalAmount,
            "https://www.ecpay.com.tw/",
            "交易測試",
            "測試商品"
        )

        var cardInfo: CardInfo? = null
        var unionPayInfo: UnionPayInfo? = null
        when(paymentUIType) {
            0 -> {
                // 信用卡定期定額
                cardInfo = CardInfo(
                    "https://www.ecpay.com.tw/",
                    totalAmount,
                    "M",
                    3,
                    5,
                    "https://www.ecpay.com.tw/"
                )
            }
            1 -> {
                // 國旅卡
                cardInfo = CardInfo(
                    redeem,
                    "https://www.ecpay.com.tw/",
                    "01012020",
                    "01012029",
                    "001"
                )
            }
            2 -> {
                // 付款選擇清單頁
                cardInfo = CardInfo(
                    redeem,
                    "https://www.ecpay.com.tw/",
                    "3,6"
                )
            }
        }

        unionPayInfo = UnionPayInfo(
            "https://www.ecpay.com.tw/"
        )

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
            consumerInfo,
            unionPayInfo
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
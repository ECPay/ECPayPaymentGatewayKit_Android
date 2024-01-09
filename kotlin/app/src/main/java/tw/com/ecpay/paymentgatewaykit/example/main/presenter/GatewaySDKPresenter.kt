package tw.com.ecpay.paymentgatewaykit.example.main.presenter

import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.util.Base64
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tw.com.ecpay.paymentgatewaykit.example.R
import tw.com.ecpay.paymentgatewaykit.example.api.ATMInfo
import tw.com.ecpay.paymentgatewaykit.example.api.BarcodeInfo
import tw.com.ecpay.paymentgatewaykit.example.api.CVSInfo
import tw.com.ecpay.paymentgatewaykit.example.api.CardInfo
import tw.com.ecpay.paymentgatewaykit.example.api.ConsumerInfo
import tw.com.ecpay.paymentgatewaykit.example.api.DecData
import tw.com.ecpay.paymentgatewaykit.example.api.GetTokenByTradeData
import tw.com.ecpay.paymentgatewaykit.example.api.GetTokenByUserData
import tw.com.ecpay.paymentgatewaykit.example.api.OrderInfo
import tw.com.ecpay.paymentgatewaykit.example.api.UnionPayInfo
import tw.com.ecpay.paymentgatewaykit.example.main.model.ExampleData
import tw.com.ecpay.paymentgatewaykit.example.main.model.GatewaySDKModel
import tw.com.ecpay.paymentgatewaykit.example.main.view.MainActivity
import tw.com.ecpay.paymentgatewaykit.example.main.view.fragment.GatewaySDKFragment
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
    private val mActivity: MainActivity,
    private val mFragment: GatewaySDKFragment,
    private val mModel: GatewaySDKModel,
    private val mExampleData: ExampleData,
    private val coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
) {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private fun registerForActivityResult() {
        activityResultLauncher =
            mFragment.registerForActivityResult(StartActivityForResult(),
                ActivityResultCallback<ActivityResult> { result ->
                    Utility.log("Activity ActivityResultCallback.onActivityResult(), resultCode:" + result.resultCode)
                    createPaymentResult(result.resultCode, result.data)
                })
    }

    fun init() {
        registerForActivityResult()
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
                        UIUtil.showAlertDialog(
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
                            "Fail Code=" + callbackData.rtnCode +
                                    ", Msg=" + callbackData.rtnMsg,
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
                            if (callbackData.callbackStatus ==
                                CallbackStatus.Success
                            ) {
                                if (callbackData.rtnCode == 1) {
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
                            if (callbackData.callbackStatus ==
                                CallbackStatus.Success
                            ) {
                                if (callbackData.rtnCode == 1) {
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
        val totalAmount = if (mModel.totalAmountSwitch.get() != null && mModel.totalAmountSwitch.get()!!) 20000 else 200
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
                    "3,6",
                    "30"
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
        getTokenByTradeInfo.rqID = System.currentTimeMillis().toString()
        getTokenByTradeInfo.revision = mExampleData.revision
        getTokenByTradeInfo.merchantID = mExampleData.merchantID
        getTokenByTradeInfo.data = base64Data

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
        getTokenByUserInfo.rqID = System.currentTimeMillis().toString()
        getTokenByUserInfo.revision = mExampleData.revision
        getTokenByUserInfo.merchantID = mExampleData.merchantID
        getTokenByUserInfo.data = base64Data

        PaymentkitManager.testGetTokenByUser(
            mActivity,
            getTokenByUserInfo, callback
        )
    }

    fun onPayment() {
        callSDKCreatePayment()
    }

    fun setTitleBarBackgroundColor() {
        val check = PaymentkitManager.setTitleBarBackgroundColor(
            mActivity,
            mModel.titleBarBackgroundColor.get()
        )
        UIUtil.showAlertDialog(mActivity, "提醒您", if (check) "設定成功" else "設定失敗",
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
                    mExampleData.appStoreName,
                    activityResultLauncher
                )
            } else {
                PaymentkitManager.createPayment(
                    mActivity,
                    mModel.token.get() ?: "",
                    xmlMerchantID ?: "",
                    languageCode,
                    useResultPage,
                    mExampleData.appStoreName,
                    activityResultLauncher
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
package tw.com.ecpay.paymentgatewaykit.example.main.model

import androidx.databinding.ObservableField

class GatewaySDKModel {

    val description = ObservableField<String>()

    val sdkVersion = ObservableField<String>()

    val token = ObservableField<String>()

    val languageSelectedPosition = ObservableField<Int>()

    val useResultPageSwitch = ObservableField<Boolean>()

    val xmlMerchantID = ObservableField<String>()

    val titleBarBackgroundColor = ObservableField<String>()
}
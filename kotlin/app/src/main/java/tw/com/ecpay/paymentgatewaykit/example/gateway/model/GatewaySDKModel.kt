package tw.com.ecpay.paymentgatewaykit.example.gateway.model

import androidx.databinding.ObservableField
import tw.com.ecpay.paymentgatewaykit.example.R

class GatewaySDKModel {

    val description = ObservableField<String>()

    val sdkVersion = ObservableField<String>()

    val getTokenType = ObservableField<Int>()

    val token = ObservableField<String>()

    val languageSelectedPosition = ObservableField<Int>()

    val useResultPageSwitch = ObservableField<Boolean>()

    val xmlMerchantID = ObservableField<String>()

    val threeDSwitch = ObservableField<Boolean>()

    fun GatewaySDKModel() {
    }

    fun init() {
        getTokenType.set(R.id.getTokenType1)
    }
}
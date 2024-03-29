package tw.com.ecpay.paymentgatewaykit.example.main.model

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

    val titleBarBackgroundColor = ObservableField<String>()

    val threeDSwitch = ObservableField<Boolean>()

    val redeemSwitch = ObservableField<Boolean>()

    val totalAmountSwitch = ObservableField<Boolean>()

    init {
        getTokenType.set(R.id.getTokenType1)
    }
}
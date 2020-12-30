package tw.com.ecpay.paymentgatewaykit.example.gateway.api

data class ConsumerInfo(
    var MerchantMemberID: String,
    var Email: String,
    var Phone: String,
    var Name: String,
    var CountryCode: String,
    var Address: String
)
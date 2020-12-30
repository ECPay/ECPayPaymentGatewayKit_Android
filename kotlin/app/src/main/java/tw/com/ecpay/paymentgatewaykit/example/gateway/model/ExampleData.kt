package tw.com.ecpay.paymentgatewaykit.example.gateway.model

data class ExampleData(
    var merchantID: String,
    var aesKey: String,
    var aesIv: String,
    var revision: String,
    var email: String,
    var phone: String,
    var name: String,
    var countryCode: String,
    var address: String,
    var appStoreName: String,
    var merchantMemberID: String
)
package tw.com.ecpay.paymentgatewaykit.example.gateway.model

data class ExampleData (
    var merchantID: String = "2000132",
    var aesKey: String = "5294y06JbISpM5x9",
    var aesIv: String = "v77hoKGq4kWxNNIS",
    var revision: String = "1.0.0",
    var email: String = "techsupport@ecpay.com.tw",
    var phone: String = "0900123456",
    var name: String = "綠界科技",
    var countryCode: String = "158",
    var address: String = "台北市南港區三重路19-2號 6號棟樓之2, D",
    var appStoreName: String = "綠界測試商店",
    var merchantMemberID: String = "123456"
)
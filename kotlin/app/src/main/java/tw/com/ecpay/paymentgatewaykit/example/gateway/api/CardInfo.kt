package tw.com.ecpay.paymentgatewaykit.example.gateway.api

data class CardInfo(
    var Redeem: String? = null,
    var OrderResultURL: String? = null,
    // ----------------------------------------- 定期定額
    var PeriodAmount: Int,
    var PeriodType: String? = null,
    var Frequency: Int,
    var ExecTimes: Int,
    var PeriodReturnURL: String? = null,
    // ----------------------------------------- 刷卡分期
    var CreditInstallment: String? = null,
    // ----------------------------------------- 國旅卡
    var TravelStartDate: String? = null,
    var TravelEndDate: String? = null,
    var TravelCounty: String? = null
)
package tw.com.ecpay.paymentgatewaykit.example.gateway.api

data class CardInfo(
    var OrderResultURL: String? = null
) {
    var Redeem: String? = null

    // ----------------------------------------- 定期定額
    var PeriodAmount: Int = 0
    var PeriodType: String? = null
    var Frequency: Int = 0
    var ExecTimes: Int = 0
    var PeriodReturnURL: String? = null

    // ----------------------------------------- 刷卡分期
    var CreditInstallment: String? = null

    //----------------------------------------- 圓夢彈性分期
    var FlexibleInstallment: String? = null

    // ----------------------------------------- 國旅卡
    var TravelStartDate: String? = null
    var TravelEndDate: String? = null
    var TravelCounty: String? = null

    constructor (
        Redeem: String,
        OrderResultURL: String,
        CreditInstallment: String,
        FlexibleInstallment: String
    ) : this(
        OrderResultURL
    ) {
        this.Redeem = Redeem
        this.CreditInstallment = CreditInstallment
        this.FlexibleInstallment = FlexibleInstallment
    }

    constructor (
        OrderResultURL: String,
        PeriodAmount: Int,
        PeriodType: String,
        Frequency: Int,
        ExecTimes: Int,
        PeriodReturnURL: String
    ) : this(
        OrderResultURL
    ) {
        this.PeriodAmount = PeriodAmount
        this.PeriodType = PeriodType
        this.Frequency = Frequency
        this.ExecTimes = ExecTimes
        this.PeriodReturnURL = PeriodReturnURL
    }

    constructor (
        Redeem: String,
        OrderResultURL: String,
        TravelStartDate: String,
        TravelEndDate: String,
        TravelCounty: String
    ) : this(
        OrderResultURL
    ) {
        this.Redeem = Redeem
        this.TravelStartDate = TravelStartDate
        this.TravelEndDate = TravelEndDate
        this.TravelCounty = TravelCounty
    }
}
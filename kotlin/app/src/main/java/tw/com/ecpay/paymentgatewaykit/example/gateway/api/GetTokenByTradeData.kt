package tw.com.ecpay.paymentgatewaykit.example.gateway.api

data class GetTokenByTradeData(
    var PlatformID: String?,
    var MerchantID: String?,
    var RememberCard: Int,
    var PaymentUIType: Int,
    var ChoosePaymentList: String?,
    var OrderInfo: OrderInfo?,
    var CardInfo: CardInfo?,
    var ATMInfo: ATMInfo?,
    var CVSInfo: CVSInfo?,
    var BarcodeInfo: BarcodeInfo?,
    var ConsumerInfo: ConsumerInfo?
)
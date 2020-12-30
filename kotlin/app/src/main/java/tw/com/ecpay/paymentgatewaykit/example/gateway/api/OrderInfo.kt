package tw.com.ecpay.paymentgatewaykit.example.gateway.api

data class OrderInfo(
    var MerchantTradeDate: String?,
    var MerchantTradeNo: String?,
    var TotalAmount: Int?,
    var ReturnURL: String?,
    var TradeDesc: String?,
    var ItemName: String?
)
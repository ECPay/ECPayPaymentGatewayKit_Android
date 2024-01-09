package tw.com.ecpay.paymentgatewaykit.example.api

data class OrderInfo(
    var MerchantTradeDate: String?,
    var MerchantTradeNo: String?,
    var TotalAmount: Int?,
    var ReturnURL: String?,
    var TradeDesc: String?,
    var ItemName: String?
)
package tw.com.ecpay.paymentgatewaykit.example.api;

public class OrderInfo {
    public String MerchantTradeDate;
    public String MerchantTradeNo;
    public int TotalAmount;
    public String ReturnURL;
    public String TradeDesc;
    public String ItemName;
    public OrderInfo(String MerchantTradeDate,
                     String MerchantTradeNo,
                     int TotalAmount,
                     String ReturnURL,
                     String TradeDesc,
                     String ItemName) {
        this.MerchantTradeDate = MerchantTradeDate;
        this.MerchantTradeNo = MerchantTradeNo;
        this.TotalAmount = TotalAmount;
        this.ReturnURL = ReturnURL;
        this.TradeDesc = TradeDesc;
        this.ItemName = ItemName;
    }
}

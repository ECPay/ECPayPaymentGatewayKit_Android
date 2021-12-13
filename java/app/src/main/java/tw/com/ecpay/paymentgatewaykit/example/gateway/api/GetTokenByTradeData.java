package tw.com.ecpay.paymentgatewaykit.example.gateway.api;

public class GetTokenByTradeData {
    public String PlatformID;
    public String MerchantID;
    public int RememberCard;
    public int PaymentUIType;
    public String ChoosePaymentList;
    public OrderInfo OrderInfo;
    public CardInfo CardInfo;
    public ATMInfo ATMInfo;
    public CVSInfo CVSInfo;
    public BarcodeInfo BarcodeInfo;
    public ConsumerInfo ConsumerInfo;
    public UnionPayInfo UnionPayInfo;
    public GetTokenByTradeData(String PlatformID,
                               String MerchantID,
                               int RememberCard,
                               int PaymentUIType,
                               String ChoosePaymentList,
                               OrderInfo OrderInfo,
                               CardInfo CardInfo,
                               ATMInfo ATMInfo,
                               CVSInfo CVSInfo,
                               BarcodeInfo BarcodeInfo,
                               ConsumerInfo ConsumerInfo,
                               UnionPayInfo UnionPayInfo) {
        this.PlatformID = PlatformID;
        this.MerchantID = MerchantID;
        this.RememberCard = RememberCard;
        this.PaymentUIType = PaymentUIType;
        this.ChoosePaymentList = ChoosePaymentList;
        this.OrderInfo = OrderInfo;
        this.CardInfo = CardInfo;
        this.ATMInfo = ATMInfo;
        this.CVSInfo = CVSInfo;
        this.BarcodeInfo = BarcodeInfo;
        this.ConsumerInfo = ConsumerInfo;
        this.UnionPayInfo = UnionPayInfo;
    }
}

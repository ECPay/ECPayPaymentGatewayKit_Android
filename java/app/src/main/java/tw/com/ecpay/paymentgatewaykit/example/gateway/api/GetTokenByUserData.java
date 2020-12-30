package tw.com.ecpay.paymentgatewaykit.example.gateway.api;

public class GetTokenByUserData {
    public String PlatformID;
    public String MerchantID;
    public ConsumerInfo ConsumerInfo;
    public GetTokenByUserData(String PlatformID,
                              String MerchantID,
                              ConsumerInfo ConsumerInfo) {
        this.PlatformID = PlatformID;
        this.MerchantID = MerchantID;
        this.ConsumerInfo = ConsumerInfo;
    }
}

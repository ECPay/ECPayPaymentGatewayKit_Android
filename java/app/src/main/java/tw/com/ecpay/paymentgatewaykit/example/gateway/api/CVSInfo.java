package tw.com.ecpay.paymentgatewaykit.example.gateway.api;

public class CVSInfo {
    public int StoreExpireDate;
    public String Desc_1;
    public String Desc_2;
    public String Desc_3;
    public String Desc_4;
    public CVSInfo(int StoreExpireDate,
                   String Desc_1,
                   String Desc_2,
                   String Desc_3,
                   String Desc_4) {
        this.StoreExpireDate = StoreExpireDate;
        this.Desc_1 = Desc_1;
        this.Desc_2 = Desc_2;
        this.Desc_3 = Desc_3;
        this.Desc_4 = Desc_4;
    }
}

package tw.com.ecpay.paymentgatewaykit.example.gateway.model;

public class ExampleData {
    private String merchantID;
    private String aesKey;
    private String aesIv;
    private String revision;
    private String email;
    private String phone;
    private String name;
    private String countryCode;
    private String address;
    private String appStoreName;
    private String merchantMemberID;
    public ExampleData() {
        this.merchantID = "2000132";
        this.aesKey = "5294y06JbISpM5x9";
        this.aesIv = "v77hoKGq4kWxNNIS";
        this.revision = "1.0.0";
        this.email = "techsupport@ecpay.com.tw";
        this.phone = "0900123456";
        this.name = "綠界科技";
        this.countryCode = "158";
        this.address = "台北市南港區三重路19-2號 6號棟樓之2, D";
        this.appStoreName = "綠界測試商店";
        this.merchantMemberID = "123456";
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getAesIv() {
        return aesIv;
    }

    public void setAesIv(String aesIv) {
        this.aesIv = aesIv;
    }

    public String getRevision() {
        return revision;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getAddress() {
        return address;
    }

    public String getAppStoreName() {
        return appStoreName;
    }

    public String getMerchantMemberID() {
        return merchantMemberID;
    }
}

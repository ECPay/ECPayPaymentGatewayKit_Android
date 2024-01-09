package tw.com.ecpay.paymentgatewaykit.example.api;

public class ConsumerInfo {
    public String MerchantMemberID;
    public String Email;
    public String Phone;
    public String Name;
    public String CountryCode;
    public String Address;
    public ConsumerInfo(String MerchantMemberID,
                        String Email,
                        String Phone,
                        String Name,
                        String CountryCode,
                        String Address) {
        this.MerchantMemberID = MerchantMemberID;
        this.Email = Email;
        this.Phone = Phone;
        this.Name = Name;
        this.CountryCode = CountryCode;
        this.Address = Address;
    }
}

package tw.com.ecpay.paymentgatewaykit.example.gateway.api;

public class CardInfo {

    /**
     * 使用信用卡紅利.
     * <br> 0:不使用紅利
     * <br> 1:使用紅利
     */
    public String Redeem;
    /**
     * Client端回傳付款結果網址.
     */
    public String OrderResultURL;

    //----------------------------------------- 定期定額
    /**
     * 定期定額，每次授權金額.
     */
    public int PeriodAmount;
    /**
     * 定期定額，週期種類.
     * <br> D：以天為週期
     * <br> M：以月為週期
     * <br> Y：以年為週期
     */
    public String PeriodType;
    /**
     * 定期定額，執行頻率.
     * <br> 至少要大於等於 1次以上
     * <br> 當 PeriodType 設為 D 時，最多可設 365次。
     * <br> 當 PeriodType 設為 M 時，最多可設 12 次。
     * <br> 當 PeriodType 設為 Y 時，最多可設 1 次。
     */
    public int Frequency;
    /**
     * 定期定額，執行次數.
     * <br> 至少要大於等於 1次以上
     * <br> 當 PeriodType 設為 D 時，最多可設 999次。
     * <br> 當 PeriodType 設為 M 時，最多可設 99 次。
     * <br> 當 PeriodType 設為 Y 時，最多可設 9 次。
     */
    public int ExecTimes;
    /**
     * 定期定額，執行結果回應URL.
     */
    public String PeriodReturnURL;

    //----------------------------------------- 刷卡分期
    /**
     * 刷卡分期期數.
     * <br> 3,6,9,12
     */
    public String CreditInstallment;

    //----------------------------------------- 國旅卡
    /**
     * 國旅卡旅遊起日.
     * <br> mmddyyyy
     */
    public String TravelStartDate;
    /**
     * 國旅卡旅遊訖日.
     * <br> mmddyyyy
     */
    public String TravelEndDate;
    /**
     * 國旅卡旅遊縣市.
     */
    public String TravelCounty;

    public CardInfo(String Redeem,
                    String OrderResultURL, String CreditInstallment) {
        this.Redeem = Redeem;
        this.OrderResultURL = OrderResultURL;
        this.CreditInstallment = CreditInstallment;
    }

    public CardInfo(String OrderResultURL,
                    int PeriodAmount,
                    String PeriodType,
                    int Frequency,
                    int ExecTimes,
                    String PeriodReturnURL) {
        this.OrderResultURL = OrderResultURL;
        this.PeriodAmount = PeriodAmount;
        this.PeriodType = PeriodType;
        this.Frequency = Frequency;
        this.ExecTimes = ExecTimes;
        this.PeriodReturnURL = PeriodReturnURL;
    }

    public CardInfo(String Redeem,
                    String OrderResultURL,
                    String TravelStartDate,
                    String TravelEndDate,
                    String TravelCounty) {
        this.Redeem = Redeem;
        this.OrderResultURL = OrderResultURL;
        this.TravelStartDate = TravelStartDate;
        this.TravelEndDate = TravelEndDate;
        this.TravelCounty = TravelCounty;
    }

}

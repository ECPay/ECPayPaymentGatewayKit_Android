package tw.com.ecpay.paymentgatewaykit.example.main.model;

import androidx.databinding.ObservableField;

public class GatewaySDKModel {

    public final ObservableField<String> description = new ObservableField<>();

    public final ObservableField<String> sdkVersion = new ObservableField<>();

    public final ObservableField<String> token = new ObservableField<>();

    public final ObservableField<Integer> languageSelectedPosition = new ObservableField<>();

    public final ObservableField<Boolean> useResultPageSwitch = new ObservableField<>();

    public final ObservableField<String> xmlMerchantID = new ObservableField<>();

    public final ObservableField<String> titleBarBackgroundColor = new ObservableField<>();
}

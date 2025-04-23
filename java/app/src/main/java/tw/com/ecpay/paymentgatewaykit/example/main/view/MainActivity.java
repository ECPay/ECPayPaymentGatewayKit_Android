package tw.com.ecpay.paymentgatewaykit.example.main.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import tw.com.ecpay.paymentgatewaykit.example.R;
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleActivityMainBinding;
import tw.com.ecpay.paymentgatewaykit.example.main.view.fragment.GatewaySDKFragment;

public class MainActivity extends AppCompatActivity {

    private PgSdkExampleActivityMainBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.pg_sdk_example_activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,
                        GatewaySDKFragment.newInstance(),
                        GatewaySDKFragment.FRAGMENT_TAG_NAME)
                .commit();
    }
}

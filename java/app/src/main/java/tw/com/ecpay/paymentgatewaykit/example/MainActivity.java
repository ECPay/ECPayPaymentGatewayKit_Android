package tw.com.ecpay.paymentgatewaykit.example;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleActivityMainBinding;
import tw.com.ecpay.paymentgatewaykit.example.gateway.fragment.GatewaySDKFragment;
import tw.com.ecpay.paymentgatewaykit.example.util.Utility;

public class MainActivity extends AppCompatActivity {

    private PgSdkExampleActivityMainBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.pg_sdk_example_activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, GatewaySDKFragment.newInstance(),
                        GatewaySDKFragment.FragmentTagName)
                .commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utility.log("MainActivity(), requestCode:" + requestCode + ", resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if(fragment!=null && requestCode == 13001) {
            (fragment).onActivityResult(requestCode, resultCode, data);
        }
    }

}

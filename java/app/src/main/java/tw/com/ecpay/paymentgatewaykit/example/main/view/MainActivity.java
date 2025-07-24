package tw.com.ecpay.paymentgatewaykit.example.main.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import tw.com.ecpay.paymentgatewaykit.example.R;
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleActivityMainBinding;
import tw.com.ecpay.paymentgatewaykit.example.main.view.fragment.GatewaySDKFragment;
import tw.com.ecpay.paymentgatewaykit.manager.util.UIManager;
import tw.com.ecpay.paymentgatewaykit.manager.util.UIViewGroupComponent;

public class MainActivity extends AppCompatActivity {

    private PgSdkExampleActivityMainBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        UIManager.activityEnableEdgeToEdge(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.pg_sdk_example_activity_main);

        initView();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,
                        GatewaySDKFragment.newInstance(),
                        GatewaySDKFragment.FRAGMENT_TAG_NAME)
                .commit();
    }

    private void initView() {
        if (UIManager.isEnableEdgeToEdge()) {
            UIManager.setEdgeToEdgeListener(
                    this,
                    getWindow().getDecorView(),
                    new UIViewGroupComponent(binding.topView, binding.topLeftView, binding.topRightView),
                    binding.bottomView,
                    binding.leftView,
                    binding.rightView
            );
        }
    }
}

package tw.com.ecpay.paymentgatewaykit.example.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import tw.com.ecpay.paymentgatewaykit.example.R
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleActivityMainBinding
import tw.com.ecpay.paymentgatewaykit.example.main.view.fragment.GatewaySDKFragment
import tw.com.ecpay.paymentgatewaykit.manager.util.UIManager
import tw.com.ecpay.paymentgatewaykit.manager.util.UIViewGroupComponent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: PgSdkExampleActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        UIManager.activityEnableEdgeToEdge(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.pg_sdk_example_activity_main
        )

        initView()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frameLayout,
                GatewaySDKFragment.newInstance(),
                GatewaySDKFragment.FragmentTagName)
            .commit()
    }

    private fun initView() {
        if (UIManager.isEnableEdgeToEdge()) {
            UIManager.setEdgeToEdgeListener(
                this,
                window.decorView,
                UIViewGroupComponent(binding.topView, binding.topLeftView, binding.topRightView),
                binding.bottomView,
                binding.leftView,
                binding.rightView
            )
        }
    }
}
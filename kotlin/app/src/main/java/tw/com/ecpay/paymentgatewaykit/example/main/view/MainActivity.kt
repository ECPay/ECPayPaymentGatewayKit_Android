package tw.com.ecpay.paymentgatewaykit.example.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import tw.com.ecpay.paymentgatewaykit.example.R
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleActivityMainBinding
import tw.com.ecpay.paymentgatewaykit.example.main.view.fragment.GatewaySDKFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: PgSdkExampleActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.pg_sdk_example_activity_main
        )

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frameLayout,
                GatewaySDKFragment.newInstance(),
                GatewaySDKFragment.FragmentTagName)
            .commit()
    }
}
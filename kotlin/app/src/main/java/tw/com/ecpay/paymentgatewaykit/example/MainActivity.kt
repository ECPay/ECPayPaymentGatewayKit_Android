package tw.com.ecpay.paymentgatewaykit.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleActivityMainBinding
import tw.com.ecpay.paymentgatewaykit.example.gateway.fragment.GatewaySDKFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: PgSdkExampleActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.pg_sdk_example_activity_main)

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.frameLayout, GatewaySDKFragment.newInstance(),
                GatewaySDKFragment.FragmentTagName)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
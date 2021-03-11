package tw.com.ecpay.paymentgatewaykit.example.gateway.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import tw.com.ecpay.paymentgatewaykit.example.R
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleFragmentGatewaysdkBinding
import tw.com.ecpay.paymentgatewaykit.example.gateway.model.ExampleData
import tw.com.ecpay.paymentgatewaykit.example.gateway.model.GatewaySDKModel
import tw.com.ecpay.paymentgatewaykit.example.gateway.presenter.GatewaySDKPresenter

class GatewaySDKFragment : Fragment() {

    companion object {

        public val FragmentTagName = GatewaySDKFragment::class.java.name

        fun newInstance(): GatewaySDKFragment {
            return GatewaySDKFragment()
        }
    }

    private lateinit var binding: PgSdkExampleFragmentGatewaysdkBinding

    private lateinit var mModel: GatewaySDKModel

    private lateinit var mPresenter: GatewaySDKPresenter

    private lateinit var mExampleData: ExampleData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate<PgSdkExampleFragmentGatewaysdkBinding>(inflater,
            R.layout.pg_sdk_example_fragment_gatewaysdk,
            container, false)
        mModel = GatewaySDKModel()
        mModel.init()
        mExampleData = ExampleData()
        mPresenter = GatewaySDKPresenter(activity!!, this, mModel, mExampleData)
        binding.mModel = mModel
        binding.mPresenter = mPresenter

        val view = binding.getRoot()

        mPresenter.init()

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mPresenter.onActivityResult(requestCode, resultCode, data)
    }

    fun showGooglePayLayout() {
        binding.googlePayLayout.visibility = View.VISIBLE
    }

    fun hideGooglePayLayout() {
        binding.googlePayLayout.visibility = View.GONE
    }

    fun showSamsungPayLayout() {
        binding.samsungPayLayout.visibility = View.VISIBLE
    }

    fun hideSamsungPayLayout() {
        binding.samsungPayLayout.visibility = View.GONE
    }

}
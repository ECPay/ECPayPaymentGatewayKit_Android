package tw.com.ecpay.paymentgatewaykit.example.main.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import tw.com.ecpay.paymentgatewaykit.example.R
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleFragmentGatewaysdkBinding
import tw.com.ecpay.paymentgatewaykit.example.main.model.GatewaySDKModel
import tw.com.ecpay.paymentgatewaykit.example.main.presenter.GatewaySDKPresenter
import tw.com.ecpay.paymentgatewaykit.example.main.view.MainActivity

class GatewaySDKFragment : Fragment() {

    companion object {

        val FragmentTagName: String = GatewaySDKFragment::class.java.name

        fun newInstance(): GatewaySDKFragment {
            return GatewaySDKFragment()
        }
    }

    private lateinit var binding: PgSdkExampleFragmentGatewaysdkBinding

    private lateinit var mModel: GatewaySDKModel

    private lateinit var mPresenter: GatewaySDKPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate<PgSdkExampleFragmentGatewaysdkBinding>(inflater,
            R.layout.pg_sdk_example_fragment_gatewaysdk,
            container, false)
        mModel = GatewaySDKModel()
        mPresenter = GatewaySDKPresenter(requireActivity() as MainActivity,
            this,
            mModel,
        )

        binding.mModel = mModel
        binding.mPresenter = mPresenter

        val view = binding.root

        initView()
        mPresenter.init()

        return view
    }

    private fun initView() {
        binding.spinnerOrientation.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                mPresenter.onOrientationItemSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}
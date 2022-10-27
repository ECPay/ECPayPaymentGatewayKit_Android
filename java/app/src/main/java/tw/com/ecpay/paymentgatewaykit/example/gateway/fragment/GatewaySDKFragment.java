package tw.com.ecpay.paymentgatewaykit.example.gateway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import tw.com.ecpay.paymentgatewaykit.example.MainActivity;
import tw.com.ecpay.paymentgatewaykit.example.R;
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleFragmentGatewaysdkBinding;
import tw.com.ecpay.paymentgatewaykit.example.gateway.model.ExampleData;
import tw.com.ecpay.paymentgatewaykit.example.gateway.model.GatewaySDKModel;
import tw.com.ecpay.paymentgatewaykit.example.gateway.presenter.GatewaySDKPresenter;

public class GatewaySDKFragment extends Fragment {

    public static final String FragmentTagName = GatewaySDKFragment.class.getName();

    private PgSdkExampleFragmentGatewaysdkBinding binding;

    private GatewaySDKModel mModel;

    private GatewaySDKPresenter mPresenter;

    private ExampleData mExampleData;

    public static GatewaySDKFragment newInstance(){
        GatewaySDKFragment oPaySDKFragment = new GatewaySDKFragment();
        return oPaySDKFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(inflater,
                R.layout.pg_sdk_example_fragment_gatewaysdk,
                container, false);
        mModel = new GatewaySDKModel();
        mExampleData = new ExampleData();
        mPresenter = new GatewaySDKPresenter((MainActivity)getActivity(), this, mModel, mExampleData);
        binding.setMModel(mModel);
        binding.setMPresenter(mPresenter);

        View view = binding.getRoot();

        mPresenter.init();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    public void showGooglePayLayout() {
        binding.googlePayLayout.setVisibility(View.VISIBLE);
    }

    public void hideGooglePayLayout() {
        binding.googlePayLayout.setVisibility(View.GONE);
    }

    public void showSamsungPayLayout() {
        binding.samsungPayLayout.setVisibility(View.VISIBLE);
    }

    public void hideSamsungPayLayout() {
        binding.samsungPayLayout.setVisibility(View.GONE);
    }

}

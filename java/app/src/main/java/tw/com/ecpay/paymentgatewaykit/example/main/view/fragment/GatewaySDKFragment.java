package tw.com.ecpay.paymentgatewaykit.example.main.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import tw.com.ecpay.paymentgatewaykit.example.R;
import tw.com.ecpay.paymentgatewaykit.example.databinding.PgSdkExampleFragmentGatewaysdkBinding;
import tw.com.ecpay.paymentgatewaykit.example.main.model.ExampleData;
import tw.com.ecpay.paymentgatewaykit.example.main.model.GatewaySDKModel;
import tw.com.ecpay.paymentgatewaykit.example.main.presenter.GatewaySDKPresenter;
import tw.com.ecpay.paymentgatewaykit.example.main.view.MainActivity;

public class GatewaySDKFragment extends Fragment {

    public static final String FragmentTagName = GatewaySDKFragment.class.getName();

    private PgSdkExampleFragmentGatewaysdkBinding binding;

    private GatewaySDKModel mModel;

    private GatewaySDKPresenter mPresenter;

    private ExampleData mExampleData;

    public static GatewaySDKFragment newInstance() {
        GatewaySDKFragment gatewaySDKFragment = new GatewaySDKFragment();
        return gatewaySDKFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(inflater,
                R.layout.pg_sdk_example_fragment_gatewaysdk,
                container, false);
        mModel = new GatewaySDKModel();
        mExampleData = new ExampleData();
        mPresenter = new GatewaySDKPresenter((MainActivity)getActivity(), this,
                mModel, mExampleData);
        binding.setMModel(mModel);
        binding.setMPresenter(mPresenter);

        View view = binding.getRoot();

        initView();
        mPresenter.init();

        return view;
    }

    private void initView() {
        binding.spinnerOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onOrientationItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}

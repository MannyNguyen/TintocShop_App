package vn.app.tintoc.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.GlobalClass;
import vn.app.tintoc.utils.ConnectivityReceiver;

public class SplashScreenFragment extends BaseFragment implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    //region Var
    LinearLayout llSplashScreen;
    LinearLayout btnStart;
    //endregion

    //region Constructor
    public SplashScreenFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Instance
    // TODO: Rename and change types and number of parameters
    public static SplashScreenFragment newInstance() {
        SplashScreenFragment fragment = new SplashScreenFragment();
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            llSplashScreen = view.findViewById(R.id.ll_splashscreen);
            btnStart = view.findViewById(R.id.btn_start);
            btnStart.setOnClickListener(this);
            checkConnection();
            isLoad = true;
        }
    }
    //endregion

    //region Methods
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        try {
            String message;
            int color;
            if (!isConnected) {
                message = "Không tìm thấy kết nối!";
                color = Color.RED;

                Snackbar snackbar = Snackbar
                        .make(view.findViewById(R.id.ll_splashscreen), message, Snackbar.LENGTH_INDEFINITE)
                        .setAction("Check", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                            }
                        });

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                snackbar.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region OnResume
    @Override
    public void onResume() {
        super.onResume();
        GlobalClass.getInstance().setConnectivityListener(this);
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
//                ((ViewGroup)view.getParent().getParent()).removeView((ViewGroup)view.getParent());
                CmmFunc.addFragment(getActivity(), R.id.main_container, ChooseServiceFragment.newInstance());
                break;
        }
    }
    //endregion
}

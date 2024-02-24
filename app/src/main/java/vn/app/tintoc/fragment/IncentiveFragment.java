package vn.app.tintoc.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import vn.app.tintoc.R;
import vn.app.tintoc.config.CmmFunc;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncentiveFragment extends BaseFragment implements View.OnClickListener {
    ImageView ivBack;

    public IncentiveFragment() {
        // Required empty public constructor
    }

    public static IncentiveFragment newInstance() {
        IncentiveFragment fragment = new IncentiveFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_incentive, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(IncentiveFragment.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
//                ivBack.setOnClickListener(null);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                            ivBack.setOnClickListener(IncentiveFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                getActivity().getSupportFragmentManager().popBackStack();
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HomeFragment.newInstance());
//                CmmFunc.pop(getActivity());
                break;
        }
    }
}

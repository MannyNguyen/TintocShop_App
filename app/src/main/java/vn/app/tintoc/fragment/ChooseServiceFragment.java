package vn.app.tintoc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import vn.app.tintoc.LoginActivity;
import vn.app.tintoc.MainActivity;
import vn.app.tintoc.R;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.helper.StorageHelper;

public class ChooseServiceFragment extends BaseFragment implements View.OnClickListener {

    //region var
    ImageView ivBackWhite;
    ImageView llNewOrder, llFollowOrder;
    //endregion

    //region constructor
    public ChooseServiceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChooseServiceFragment newInstance() {
        ChooseServiceFragment fragment = new ChooseServiceFragment();
        return fragment;
    }
    //endregion

    //region OnCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        view = inflater.inflate(R.layout.fragment_choose, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivBackWhite = view.findViewById(R.id.iv_back_white);
            llNewOrder = view.findViewById(R.id.ll_new_order);
            llFollowOrder = view.findViewById(R.id.ll_follow_order);
            view.findViewById(R.id.iv_back_white).setOnClickListener(this);

            llNewOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (StorageHelper.get(StorageHelper.TOKEN).equals("")) {
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getContext(), MainActivity.class);
                        startActivity(i);
                    }
                }
            });

            llFollowOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CmmFunc.addFragment(getActivity(), R.id.main_container, FollowOrderDialogFragment.newInstance());
                }
            });
            isLoad = true;
        }
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_white:
//                ivBackWhite.setOnClickListener(null);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                            ivBackWhite.setOnClickListener(ChooseServiceFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                getActivity().getSupportFragmentManager().popBackStack();
////                CmmFunc.pop(getActivity());
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, SplashScreenFragment.newInstance());
                break;
        }
    }
    //endregion

}

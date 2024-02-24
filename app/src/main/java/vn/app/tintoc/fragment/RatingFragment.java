package vn.app.tintoc.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import vn.app.tintoc.R;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.CircleTransform;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingFragment extends BaseFragment implements View.OnClickListener {
    ImageView ivShipperAvatar;
    ImageView ivBack;
    EditText edtRatingNote;
    LinearLayout llRatingCancel, llRatingSend;
    String noteRating;
    String userName = StorageHelper.get(StorageHelper.USERNAME);
    String token = StorageHelper.get(StorageHelper.TOKEN);

    private static final String urlAvatarShipper = "http://hermesmercury.weebly.com/uploads/1/6/6/0/16609534/1473479_orig.jpg";

    public RatingFragment() {
        // Required empty public constructor
    }

    public static RatingFragment newInstance() {
        RatingFragment fragment = new RatingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_rating, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivShipperAvatar = view.findViewById(R.id.iv_shipper_avatar);
            ivBack = view.findViewById(R.id.iv_back);
            edtRatingNote = view.findViewById(R.id.edt_rating_note);
            llRatingCancel = view.findViewById(R.id.ll_rating_cancel);
            llRatingSend = view.findViewById(R.id.ll_rating_send);
            getView().findViewById(R.id.iv_back).setOnClickListener(RatingFragment.this);
            getView().findViewById(R.id.ll_rating_cancel).setOnClickListener(RatingFragment.this);
            getView().findViewById(R.id.ll_rating_send).setOnClickListener(RatingFragment.this);
            isLoad = true;
        }
        loadAvatarShipper();
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
//                            ivBack.setOnClickListener(RatingFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                getActivity().getSupportFragmentManager().popBackStack();
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HomeFragment.newInstance());
//                CmmFunc.pop(getActivity());
                break;
            case R.id.ll_rating_cancel:
                edtRatingNote.setText("Ghi chú:");
                break;
            case R.id.ll_rating_send:
                noteRating = edtRatingNote.getText().toString();
                new SendRatingShipper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }

    private void loadAvatarShipper() {
        // Loading profile image
        Glide.with(this).load(urlAvatarShipper).crossFade().thumbnail(0.5f).bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivShipperAvatar);
    }

    class SendRatingShipper extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
//                String value = APIHelper.ratingShipper(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
//                        userName, token, orderCode, noteRating);
//                jsonObject = new JSONObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    //Show toast here
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

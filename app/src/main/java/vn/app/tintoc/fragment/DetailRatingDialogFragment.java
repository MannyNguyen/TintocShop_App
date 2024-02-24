package vn.app.tintoc.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import vn.app.tintoc.R;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.Utility;

import org.json.JSONObject;

/**
 * Created by Admin on 9/22/2017.
 */

public class DetailRatingDialogFragment extends BaseFragment implements View.OnClickListener {

    //region Var
    EditText edtRating;
    Button btnRating;
    ImageView btnBack;
    FrameLayout frameRating;
    //endregion

    //region Constructor
    public DetailRatingDialogFragment() {
    }
    //endregion

    //region NewInstance
    public static DetailRatingDialogFragment newInstance(String order_code) {
        Bundle args = new Bundle();
        args.putString("order_code", order_code);
        DetailRatingDialogFragment fragment = new DetailRatingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.detail_rating_dialog_fragment, container, false);

        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            edtRating = view.findViewById(R.id.edt_rate);
            btnRating = view.findViewById(R.id.btn_rate);
            btnBack = view.findViewById(R.id.iv_back);
            frameRating = view.findViewById(R.id.frame_rating);
            getView().findViewById(R.id.iv_back).setOnClickListener(this);
            getView().findViewById(R.id.btn_rate).setOnClickListener(this);

            frameRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            isLoad = true;
        }
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                btnBack.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            btnBack.setOnClickListener(DetailRatingDialogFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_rate:
                int numberStar = ((RatingBar) view.findViewById(R.id.rating)).getProgress();
                int numberStar1 = ((RatingBar) view.findViewById(R.id.rating1)).getProgress();
                String note = edtRating.getText().toString();
                new SendRatingShipper().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, numberStar, numberStar1, note);
                break;
        }
    }
    //endregion

    //region Request API

    //region Send rating
    class SendRatingShipper extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                int numberStar = (int) params[0];
                int numberStar1 = (int) params[1];
                String note = (String) params[2];
                String value = APIHelper.ratingShipper(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), getArguments().getString("order_code"), numberStar, numberStar1, note);
                jsonObject = new JSONObject(value);
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
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion
}

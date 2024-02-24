package vn.app.tintoc.fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import vn.app.tintoc.R;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends BaseFragment implements View.OnClickListener {

    //region Var
    LinearLayout txtSpHotLine_1;
    LinearLayout txtSpHotline_2;
    ImageView ivBack;
    EditText edtContentSupport;
    LinearLayout llSupportSend;
    String contentSupport;
    //endregion

    //region Constructor
    public SupportFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Instance
    public static SupportFragment newInstance() {
        SupportFragment fragment = new SupportFragment();
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
        view = inflater.inflate(R.layout.fragment_support, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            txtSpHotLine_1 = view.findViewById(R.id.txt_sphotline_1);
            txtSpHotline_2 = view.findViewById(R.id.txt_sphotline_2);
            ivBack = view.findViewById(R.id.iv_back);
            edtContentSupport = view.findViewById(R.id.edt_content_support);
            llSupportSend = view.findViewById(R.id.ll_support_send);
            getView().findViewById(R.id.iv_back).setOnClickListener(SupportFragment.this);
            getView().findViewById(R.id.txt_sphotline_1).setOnClickListener(SupportFragment.this);
            getView().findViewById(R.id.txt_sphotline_2).setOnClickListener(SupportFragment.this);
            getView().findViewById(R.id.ll_support_send).setOnClickListener(SupportFragment.this);
            getView().findViewById(R.id.btn_feedback).setOnClickListener(this);
            getView().findViewById(R.id.btn_send_request).setOnClickListener(this);

            edtContentSupport.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String content = edtContentSupport.getText().toString();
                    if (content.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtContentSupport.setTypeface(customFont);
                        return;
                    } else {
                        if (edtContentSupport.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtContentSupport.setTypeface(customFont);
                    }
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
            case R.id.iv_back:
//                ivBack.setOnClickListener(null);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                            ivBack.setOnClickListener(SupportFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                getActivity().getSupportFragmentManager().popBackStack();
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HomeFragment.newInstance());
//                CmmFunc.pop(getActivity());
                break;
            case R.id.txt_sphotline_1:
                Intent iHotline_1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getString(R.string.hotline_number), null));
                startActivity(iHotline_1);
                break;
            case R.id.txt_sphotline_2:
                Intent iHotline_2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getString(R.string.hotline_number_2), null));
                startActivity(iHotline_2);
                break;
            case R.id.ll_support_send:
//                contentSupport = edtContentSupport.getText().toString();
//                new GetSupportApi().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.btn_feedback:
                if (edtContentSupport.getText().toString().equals("")) {
                    edtContentSupport.setError(getString(R.string.err_content));
                    edtContentSupport.requestFocus();
                    return;
                }
                contentSupport = edtContentSupport.getText().toString();
                new GetRequestSupportApi().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.btn_send_request:
                if (edtContentSupport.getText().toString().equals("")) {
                    edtContentSupport.setError(getString(R.string.err_content));
                    edtContentSupport.requestFocus();
                    return;
                }
                contentSupport = edtContentSupport.getText().toString();
                new GetSupportApi().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }
    //endregion

    //region Request API

    //region Gửi yêu cầu
    class GetSupportApi extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.sendRequestSupport(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), contentSupport);
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
                    ///Show toast
                    Toast.makeText(getContext(), getString(R.string.success_feedback), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Gửi phản hồi
    class GetRequestSupportApi extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.sendResponse(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), contentSupport);
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
                    Toast.makeText(getContext(), getString(R.string.success_feedback), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion
}

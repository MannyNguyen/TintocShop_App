package vn.app.tintoc.fragment;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import vn.app.tintoc.R;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.Utility;

/**
 * Created by IPP on 12/7/2017.
 */

public class ChangePasswordDialogFragment extends BaseFragment implements View.OnClickListener {

    ImageView ivClose;
    EditText edtOldPass, edtNewPass, edtReNewPass;
    FrameLayout frameChangePass;
    Button btnChangePass;

    public static ChangePasswordDialogFragment newInstance() {
        Bundle args = new Bundle();
        ChangePasswordDialogFragment fragment = new ChangePasswordDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.change_password_dialogfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivClose = view.findViewById(R.id.iv_close);
            frameChangePass = view.findViewById(R.id.frame_change_pass);
            edtOldPass = view.findViewById(R.id.edt_old_pass);
            edtNewPass = view.findViewById(R.id.edt_new_pass);
            edtReNewPass = view.findViewById(R.id.edt_re_new_pass);
            btnChangePass = view.findViewById(R.id.btn_change_pass);
            ivClose.setOnClickListener(this);
            btnChangePass.setOnClickListener(this);

            frameChangePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            isLoad = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                ivClose.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
//                            ivClose.setOnClickListener(ChangePasswordDialogFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.btn_change_pass:
                if (edtOldPass.getText().toString().equals("")) {
                    edtOldPass.setError(getString(R.string.err_old_pass));
                    Toast.makeText(getContext(), getString(R.string.err_old_pass), Toast.LENGTH_SHORT).show();
                    edtOldPass.requestFocus();
                    return;
                }
                if (edtNewPass.getText().toString().equals("")) {
                    edtOldPass.setError(getString(R.string.err_new_pass));
                    Toast.makeText(getContext(), getString(R.string.err_new_pass), Toast.LENGTH_SHORT).show();
                    edtNewPass.requestFocus();
                    return;
                }
                if (edtReNewPass.getText().toString().equals("")) {
                    edtReNewPass.setError(getString(R.string.err_confirm_new_pass));
                    Toast.makeText(getContext(), getString(R.string.err_confirm_new_pass), Toast.LENGTH_SHORT).show();
                    edtReNewPass.requestFocus();
                    return;
                }
                if (!edtNewPass.getText().toString().equals(edtReNewPass.getText().toString())) {
                    edtNewPass.requestFocus();
                    edtReNewPass.requestFocus();
                    Toast.makeText(getContext(), getString(R.string.err_old_new), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtNewPass.getText().toString().length() < 6) {
                    edtNewPass.setError(getString(R.string.err_6_char));
                    edtNewPass.requestFocus();
                    Toast.makeText(getContext(), getString(R.string.err_6_char), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtReNewPass.getText().toString().length() < 6) {
                    edtReNewPass.setError(getString(R.string.err_6_char));
                    edtReNewPass.requestFocus();
                    Toast.makeText(getContext(), getString(R.string.err_6_char), Toast.LENGTH_SHORT).show();
                    return;
                }
                String oldpass = edtOldPass.getText().toString();
                String newpass = edtNewPass.getText().toString();
                new ChangePassword().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, oldpass, newpass);
                break;
        }
    }

    //region Request API
    class ChangePassword extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String oldpass = (String) objects[0];
                String newpass = (String) objects[1];
                String value = APIHelper.getChangePassword(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), oldpass, newpass);
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
                    JSONObject data = jsonObject.getJSONObject("data");
                    StorageHelper.set(StorageHelper.TOKEN, data.getString("token"));
                    Toast.makeText(getContext(), getString(R.string.change_pass_success), Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion
}

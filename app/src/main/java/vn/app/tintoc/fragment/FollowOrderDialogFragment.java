package vn.app.tintoc.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import vn.app.tintoc.R;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.Utility;

import org.json.JSONObject;

/**
 * Created by Admin on 8/2/2017.
 */

public class FollowOrderDialogFragment extends BaseFragment implements View.OnClickListener {

    //region var
    EditText edtFollowEmail, edtFollowPassword, edtEnterOrderCode;
    Button btnFollowOk;
    FrameLayout frameFollow;
    TextView txtQuickRegister;
    LinearLayout llNextFollow;
    private String email, password, orderCode;
    String userName, token;
    ImageView ivClose;
    //endregion

    //region Constructor
    public FollowOrderDialogFragment() {
    }
    //endregion

    //region newInstance
    public static FollowOrderDialogFragment newInstance() {
        Bundle args = new Bundle();
        FollowOrderDialogFragment fragment = new FollowOrderDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.followorder_dialogfragment, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivClose = view.findViewById(R.id.iv_close);
            frameFollow = view.findViewById(R.id.frame_follow);
            edtFollowEmail = view.findViewById(R.id.edt_follow_email);
            edtFollowPassword = view.findViewById(R.id.edt_follow_password);
            edtEnterOrderCode = view.findViewById(R.id.edt_enter_order_code);
            btnFollowOk = view.findViewById(R.id.btn_ok_follow);
            txtQuickRegister = view.findViewById(R.id.txt_quick_register);
            llNextFollow = view.findViewById(R.id.ll_next_follow);
            edtEnterOrderCode.setEnabled(false);
            llNextFollow.setEnabled(false);
            ivClose.setOnClickListener(this);
            btnFollowOk.setOnClickListener(this);
            txtQuickRegister.setOnClickListener(this);
            llNextFollow.setOnClickListener(this);

            frameFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            if (StorageHelper.get(StorageHelper.GUEST_TOKEN).equals("")) {
                edtEnterOrderCode.setEnabled(false);
                llNextFollow.setEnabled(false);
                llNextFollow.setBackgroundResource(R.drawable.gray_box);
                llNextFollow.setEnabled(false);
                edtFollowEmail.setEnabled(true);
                edtFollowPassword.setEnabled(true);
                btnFollowOk.setVisibility(View.VISIBLE);
                txtQuickRegister.setVisibility(View.VISIBLE);
            } else {
                llNextFollow.setEnabled(false);
                llNextFollow.setBackgroundResource(R.drawable.blue_box);
                llNextFollow.setEnabled(true);
                edtFollowEmail.setText(StorageHelper.get(StorageHelper.GUEST_EMAIL));
                edtFollowPassword.setText("......");
                edtEnterOrderCode.setEnabled(true);
                edtFollowPassword.setVisibility(View.GONE);
                btnFollowOk.setVisibility(View.GONE);
                txtQuickRegister.setVisibility(View.GONE);
            }

            llNextFollow.setOnClickListener(this);

            //region setting font edittext
            edtFollowEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String email = edtFollowEmail.getText().toString();
                    if (email.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtFollowEmail.setTypeface(customFont);
                        return;
                    } else {
                        if (edtFollowEmail.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtFollowEmail.setTypeface(customFont);
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            edtFollowPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String pass = edtFollowPassword.getText().toString();
                    if (pass.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtFollowPassword.setTypeface(customFont);
                        return;
                    } else {
                        if (edtFollowPassword.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtFollowPassword.setTypeface(customFont);
                    }
                }
            });

            edtEnterOrderCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String code = edtEnterOrderCode.getText().toString();
                    if (code.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtEnterOrderCode.setTypeface(customFont);
                        return;
                    } else {
                        if (edtEnterOrderCode.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtEnterOrderCode.setTypeface(customFont);
                    }
                }
            });
            //endregion

            isLoad = true;
        }
    }
    //endregion

    //region OnClick
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
                            ivClose.setOnClickListener(FollowOrderDialogFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                //Ẩn bàn phím
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.btn_ok_follow:
                if (edtFollowEmail.getText().toString().equals("")) {
                    edtFollowEmail.setError(getString(R.string.err_email));
                    edtFollowEmail.requestFocus();
                    return;
                }
                if (edtFollowPassword.getText().toString().equals("")) {
                    edtFollowPassword.setError(getString(R.string.err_pass));
                    edtFollowPassword.requestFocus();
                    return;
                }
                email = edtFollowEmail.getText().toString();
                password = edtFollowPassword.getText().toString();
                new GetLoginAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.txt_quick_register:
                if (edtFollowEmail.getText().toString().equals("")) {
                    edtFollowEmail.setError(getString(R.string.err_email));
                    edtFollowEmail.requestFocus();
                    return;
                }
                if (edtFollowPassword.getText().toString().equals("")) {
                    edtFollowPassword.setError(getString(R.string.err_pass));
                    edtFollowPassword.requestFocus();
                    return;
                }
                email = edtFollowEmail.getText().toString();
                password = edtFollowPassword.getText().toString();
                new GetQuickAccess().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.ll_next_follow:
                if (edtEnterOrderCode.getText().toString().trim().equals("")) {
                    edtEnterOrderCode.setError(getString(R.string.err_code));
                    edtEnterOrderCode.requestFocus();
                    return;
                }
                //Gọi API GetOrderByCode trước khi chuyển sang màn hình detail để kiểm tra orderCode user nhập có đúng không, nếu đúng mới chuyển màn hình
                orderCode = edtEnterOrderCode.getText().toString();
                new GetOrderByCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }
    //endregion

    //region Method

    //region Hide keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    //endregion

    //endregion

    //region Request API

    //region guest login
    class GetLoginAPI extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.guestLogin(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        email, password);
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
                    //Save username and token into Shared preference
                    token = data.getString("token");
                    userName = data.getString("username");
                    StorageHelper.set(StorageHelper.GUEST_EMAIL, userName);
                    StorageHelper.set(StorageHelper.GUEST_TOKEN, token);
                    Toast.makeText(getActivity(), getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show();
                    hideSoftKeyboard(getActivity());
                    edtFollowEmail.setEnabled(false);
                    edtFollowPassword.setEnabled(false);
                    edtEnterOrderCode.setEnabled(true);
                    btnFollowOk.setEnabled(false);
                    btnFollowOk.setBackgroundResource(R.drawable.gray_box);
                    txtQuickRegister.setVisibility(View.GONE);
                    llNextFollow.setEnabled(true);
                    llNextFollow.setBackgroundResource(R.drawable.blue_box);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Register guest
    class GetQuickAccess extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.quickRegister(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), email, password);
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
                    //Save username and token into Shared preference
                    token = data.getString("token");
                    userName = data.getString("username");
                    StorageHelper.set(StorageHelper.GUEST_EMAIL, userName);
                    StorageHelper.set(StorageHelper.GUEST_TOKEN, token);
                    Toast.makeText(getActivity(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                    hideSoftKeyboard(getActivity());
                    edtFollowEmail.setEnabled(false);
                    edtFollowPassword.setEnabled(false);
                    btnFollowOk.setBackgroundResource(R.drawable.gray_box);
                    txtQuickRegister.setVisibility(View.GONE);
                    edtEnterOrderCode.setEnabled(true);
                    llNextFollow.setEnabled(true);
                    llNextFollow.setBackgroundResource(R.drawable.blue_box);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Request order
    class GetOrderByCode extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getGuestOrderByCode(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.GUEST_EMAIL), StorageHelper.get(StorageHelper.GUEST_TOKEN), orderCode);
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
                    CmmFunc.addFragment(getActivity(), R.id.main_container, ShopOrderDetailFragment.newInstance(orderCode,
                            StorageHelper.get(StorageHelper.GUEST_EMAIL), StorageHelper.get(StorageHelper.GUEST_TOKEN), true));
//                    llNextFollow.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (edtEnterOrderCode.getText().toString().trim().equals("")) {
//                                edtEnterOrderCode.setError(getString(R.string.err_code));
//                                edtEnterOrderCode.requestFocus();
//                                return;
//                            }
//                            orderCode = edtEnterOrderCode.getText().toString();
//                            //Sau khi xác nhận orderCode user nhập đúng (code=1) thì thực hiện chuyển màn hình
//                            CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopOrderDetailFragment.newInstance(orderCode,
//                                    userName, token, true));
//                        }
//                    });
                } else {
                    Toast.makeText(getContext(), "Vui lòng kiểm tra lại mã đơn hàng", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion
}

package vn.app.tintoc.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import vn.app.tintoc.MainActivity;
import vn.app.tintoc.R;
import vn.app.tintoc.adapter.RegionRegisterAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.config.GlobalClass;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.RegionObj;
import vn.app.tintoc.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    //region Var
    TextView txtLoginNow;
    Spinner spnCity, spnCounty, spnWard;
    EditText edtUserName, edtPhoneNum, edtEmail, edtRegisterPassword, edtConfirmPassword, edtPlace;
    String userName, phoneNum, email, registerPassword, confirmPassword, place;
    Button btnRegister;
    private int idCity, idCounty, idWard;
    private String nameCity, nameCounty, nameWard;
    //endregion

    //region Constructor
    public RegisterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            txtLoginNow = view.findViewById(R.id.txt_login_now);
            spnCity = view.findViewById(R.id.spn_city);
            spnCounty = view.findViewById(R.id.spn_county);
            spnWard = view.findViewById(R.id.spn_ward);
            edtUserName = view.findViewById(R.id.edt_username);
            edtPhoneNum = view.findViewById(R.id.edt_phone_num);
            edtEmail = view.findViewById(R.id.edt_email);
            edtRegisterPassword = view.findViewById(R.id.edt_register_password);
            edtConfirmPassword = view.findViewById(R.id.edt_confirm_password);
            edtPlace = view.findViewById(R.id.edt_place);
            btnRegister = view.findViewById(R.id.btn_register);
            txtLoginNow = view.findViewById(R.id.txt_login_now);

            txtLoginNow.setOnClickListener(this);
            btnRegister.setOnClickListener(this);

            //region edittext hint in nghiêng, text nhập in thường
            edtEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //font của hint
                    String email = edtEmail.getText().toString();
                    if (email.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtEmail.setTypeface(customFont);
                        return;
                    } else {
                        if (edtEmail.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtEmail.setTypeface(customFont);
                    }
                }
            });

            edtUserName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String userName = edtUserName.getText().toString();
                    if (userName.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtUserName.setTypeface(customFont);
                        return;
                    } else {
                        if (edtUserName.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtUserName.setTypeface(customFont);
                    }
                }
            });

            edtPhoneNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String phoneNum = edtPhoneNum.getText().toString();
                    if (phoneNum.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtPhoneNum.setTypeface(customFont);
                        return;
                    } else {
                        if (edtPhoneNum.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtPhoneNum.setTypeface(customFont);
                    }
                }
            });

            edtRegisterPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String registerpass = edtRegisterPassword.getText().toString();
                    if (registerpass.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtRegisterPassword.setTypeface(customFont);
                        return;
                    } else {
                        if (edtRegisterPassword.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtRegisterPassword.setTypeface(customFont);
                    }
                }
            });

            edtConfirmPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String confirmPsss = edtConfirmPassword.getText().toString();
                    if (confirmPsss.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtConfirmPassword.setTypeface(customFont);
                        return;
                    } else {
                        if (edtConfirmPassword.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtConfirmPassword.setTypeface(customFont);
                    }
                }
            });

            edtPlace.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String place = edtPlace.getText().toString();
                    if (place.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtPlace.setTypeface(customFont);
                        return;
                    } else {
                        if (edtPlace.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtPlace.setTypeface(customFont);
                    }
                }
            });
            //endregion

            //region Spinner city
            spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Update quan huyen
                    RegionObj obj = (RegionObj) parent.getSelectedItem();
                    idCity = obj.getId();
                    nameCity = obj.getName();
                    new getPlaceAPIDistrict().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2, obj.getId(), 0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //endregion

            //region Spinner county
            spnCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Update country
                    RegionObj obj = (RegionObj) parent.getSelectedItem();
                    idCounty = obj.getId();
                    nameCounty = obj.getName();
                    new getPlaceWard().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 3, idCity, obj.getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //endregion

            //region Spinner ward
            spnWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //update ward
                    RegionObj obj = (RegionObj) parent.getSelectedItem();
                    idWard = obj.getId();
                    nameWard = obj.getName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //endregion

            new getPlaceAPICity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 1, 0, 0);

            isLoad = true;
        }
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_login_now:
//                getActivity().getSupportFragmentManager().popBackStack();
                CmmFunc.replaceFragment(getActivity(), R.id.login_container, new LoginFragment());
                break;
            case R.id.btn_register:
                if (edtUserName.getText().toString().equals("")) {
                    edtUserName.setError(getString(R.string.err_name));
                    edtUserName.requestFocus();
                    return;
                }
                if (edtPhoneNum.getText().toString().equals("")) {
                    edtPhoneNum.setError(getString(R.string.err_phone));
                    edtPhoneNum.requestFocus();
                    return;
                }
                if (edtEmail.getText().toString().equals("")) {
                    edtEmail.setError(getString(R.string.err_email));
                    edtEmail.requestFocus();
                    return;
                }
                if (edtRegisterPassword.getText().toString().equals("")) {
                    edtRegisterPassword.setError(getString(R.string.err_pass));
                    edtRegisterPassword.requestFocus();
                    return;
                }
                if (edtConfirmPassword.getText().toString().equals("")) {
                    edtConfirmPassword.setError(getString(R.string.err_confirm_pass));
                    edtConfirmPassword.requestFocus();
                    return;
                }
                if (edtPlace.getText().toString().equals("")) {
                    edtPlace.setError(getString(R.string.err_confirm_pass));
                    edtPlace.requestFocus();
                    return;
                }
                if (edtRegisterPassword.getText().toString().length() < 6) {
                    edtRegisterPassword.setError(getString(R.string.err_password));
                    edtRegisterPassword.requestFocus();
                    return;
                }
                if (edtConfirmPassword.getText().toString().length() < 6) {
                    edtConfirmPassword.setError(getString(R.string.err_password));
                    edtConfirmPassword.requestFocus();
                    return;
                }
                if (!edtRegisterPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                    Toast.makeText(getContext(), getString(R.string.er_pass_confirm), Toast.LENGTH_SHORT).show();
                    return;
                }
                userName = edtUserName.getText().toString();
                phoneNum = edtPhoneNum.getText().toString();
                email = edtEmail.getText().toString();
                registerPassword = edtRegisterPassword.getText().toString();
                confirmPassword = edtConfirmPassword.getText().toString();
                place = edtPlace.getText().toString();

                GlobalClass.setActivity(getActivity());
                new GetRegisterAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
        }
    }
    //endregion

    //region Request API
    class getPlaceAPICity extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                int typeRegion = (int) params[0];
                int cityId = (int) params[1];
                int districtId = (int) params[2];
                String value = APIHelper.getRegionActiveAPI(typeRegion, cityId, districtId);
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
                    List<RegionObj> regionObjList = (List<RegionObj>) CmmFunc.tryParseList(jsonObject.getString("data"),
                            RegionObj.class);
                    RegionRegisterAdapter regionAdapter = new RegionRegisterAdapter((FragmentActivity) getContext(),
                            (FragmentActivity) getContext(), R.layout.row_spinner_region, regionObjList);
                    spnCity.setAdapter(regionAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class getPlaceAPIDistrict extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                int typeRegion = (int) params[0];
                int cityId = (int) params[1];
                int districtId = (int) params[2];
                String value = APIHelper.getRegionAPI(typeRegion, cityId, districtId);
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
                    List<RegionObj> regionObjList = (List<RegionObj>) CmmFunc.tryParseList(jsonObject.getString("data"),
                            RegionObj.class);
                    RegionRegisterAdapter regionAdapter = new RegionRegisterAdapter((FragmentActivity) getContext(), (FragmentActivity) getContext(),
                            R.layout.row_spinner_register_region, regionObjList);
                    spnCounty.setAdapter(regionAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getPlaceWard extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                int typeRegion = (int) params[0];
                int cityId = (int) params[1];
                int districtId = (int) params[2];
                String value = APIHelper.getRegionAPI(typeRegion, cityId, districtId);
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
                    List<RegionObj> regionObjList = (List<RegionObj>) CmmFunc.tryParseList(jsonObject.getString("data"), RegionObj.class);
                    RegionRegisterAdapter regionAdapter = new RegionRegisterAdapter((FragmentActivity) getContext(), (FragmentActivity) getContext(),
                            R.layout.row_spinner_register_region, regionObjList);
                    spnWard.setAdapter(regionAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class GetRegisterAPI extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.registerAPI(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), email,
                        registerPassword, confirmPassword, userName, phoneNum, place, idCity, nameCity, idCounty, nameCounty,
                        idWard, nameWard, Config.SOURCE_NORMAL);
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
                    StorageHelper.set(StorageHelper.USERNAME, data.getString("username"));
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage(getString(R.string.register_success));
                    alertDialogBuilder.setPositiveButton(getString(R.string.next), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("ALLOW_CREATE", 1);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion
}

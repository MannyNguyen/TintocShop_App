package vn.app.tintoc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import vn.app.tintoc.R;
import vn.app.tintoc.adapter.RegionAdapter;
import vn.app.tintoc.adapter.ShopAddressAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.HttpHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.RegionObj;
import vn.app.tintoc.model.ShopAddressObj;
import vn.app.tintoc.utils.CircleTransform;
import vn.app.tintoc.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopInfoFragment extends BaseFragment implements View.OnClickListener {

    //region var
    EditText edtShopInfoCode, edtShopInfoName, edtShopInfoEmail, edtNumAccBank, edtNameAccBank, edtBankName;
    TextView txtFullName, txtChangePassword, txtAddAddress;
    ImageView imgBack, ivHeaderPhoto;
    LinearLayout llAgree;
    Spinner spnShopInfoCity, spnShopInfoWard, spnShopInfoCounty;
    private int idCity, idCounty, idWard;
    int shopCity, shopDistrict, shopWard;
    private String nameCity, nameCounty, nameWard;
    RecyclerView recyclerListAddress;
    String urlNavHeaderBg;
    List<ShopAddressObj> shopAddressObjs;
    //endregion

    //region constructor
    public ShopInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ShopInfoFragment newInstance() {
        ShopInfoFragment fragment = new ShopInfoFragment();
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
        view = inflater.inflate(R.layout.fragment_shop_info, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
//            loadNavigationHeader();
            ivHeaderPhoto = view.findViewById(R.id.avatar);
            txtFullName = view.findViewById(R.id.name);
            edtNumAccBank = view.findViewById(R.id.edt_num_acc_bank);
            edtNameAccBank = view.findViewById(R.id.edt_name_acc_bank);
            edtBankName = view.findViewById(R.id.edt_bank_name);
            spnShopInfoCity = view.findViewById(R.id.spn_shopinfo_city);
            spnShopInfoCounty = view.findViewById(R.id.spn_shopinfo_county);
            spnShopInfoWard = view.findViewById(R.id.spn_shopinfo_ward);
            edtShopInfoCode = view.findViewById(R.id.edt_shopinfo_code);
            edtShopInfoName = view.findViewById(R.id.edt_shopinfo_name);
            edtShopInfoEmail = view.findViewById(R.id.edt_shopinfo_email);
            txtChangePassword = view.findViewById(R.id.txt_change_password);
            txtAddAddress = view.findViewById(R.id.txt_add_address);
            recyclerListAddress = view.findViewById(R.id.recycler_list_address);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerListAddress.setLayoutManager(layoutManager);
            llAgree = view.findViewById(R.id.ll_agree);
            llAgree.setOnClickListener(ShopInfoFragment.this);
            imgBack = view.findViewById(R.id.iv_back);
            imgBack.setOnClickListener(ShopInfoFragment.this);
            txtChangePassword.setOnClickListener(this);
            txtAddAddress.setOnClickListener(this);
            new GetProfileApi().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            //region spinner shop info city
            spnShopInfoCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

            //region shop info county
            spnShopInfoCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

            //region spinner info ward
            spnShopInfoWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

            //region add text change to set italic text

            edtShopInfoName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String shopName = edtShopInfoName.getText().toString();
                    if (shopName.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtShopInfoName.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtShopInfoName.setTypeface(customFont);
                    }
                }
            });

            edtShopInfoEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String email = edtShopInfoEmail.getText().toString();
                    if (email.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtShopInfoEmail.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtShopInfoEmail.setTypeface(customFont);
                    }
                }
            });
            //endregion

            isLoad = true;
        }
    }
    //endregion

    //region ActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1999 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                Bitmap bmp = null;
                Uri uri = Uri.parse(data.getDataString());
                if (uri != null) {
                    String realPath = CmmFunc.getPathFromUri(getActivity(), uri);
                    if (realPath != null) {
                        ExifInterface exif = new ExifInterface(realPath);
                        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        switch (rotation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotation = 90;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotation = 180;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotation = 270;
                                break;
                            default:
                                rotation = 0;
                                break;
                        }
                        Matrix matrix = new Matrix();
                        if (rotation != 0f) {
                            matrix.preRotate(rotation);
                        }
                        bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                    } else {
                        if (data.getData() == null) {
                            bmp = (Bitmap) data.getExtras().get("data");
                        } else {
                            InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            bmp = BitmapFactory.decodeStream(bufferedInputStream);
                        }
                    }
                } else {
                    if (data.getData() == null) {
                        bmp = (Bitmap) data.getExtras().get("data");
                    } else {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    }
                }
                bmp = CmmFunc.scaleDown(bmp, 720, true);
                File file = CmmFunc.bitmapToFile(getActivity(), bmp);
                updateAvatar(file);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Method
    private void updateAvatar(final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map.Entry<String, String>> params = new ArrayList<>();
                    params.add(new AbstractMap.SimpleEntry("country_code", Config.COUNTRY_CODE));
                    params.add(new AbstractMap.SimpleEntry("deviceid", Utility.getDeviceID(getContext())));
                    params.add(new AbstractMap.SimpleEntry("username", StorageHelper.get(StorageHelper.USERNAME)));
                    params.add(new AbstractMap.SimpleEntry("token", StorageHelper.get(StorageHelper.TOKEN)));
                    String response = HttpHelper.postFile(Config.BASE_URL + Config.DOMAIN_UPDATE_AVATAR, params, "avatar", file);
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Round ảnh thành background tròn, update cho left menu
                                Glide.with(getActivity()).load(file).transform(new CircleTransform(getActivity())).into(ivHeaderPhoto);
                                ImageView avatar = getActivity().findViewById(R.id.iv_header_photo);
                                Glide.with(getActivity()).load(file).transform(new CircleTransform(getActivity())).into(avatar);

                            }
                        });
                    }
                } catch (Exception e) {

                }
            }
        }).start();
    }

    private void updateprofile(final String fullName, final String email, final String account_bank,
                               final String name_bank, final String ctk_bank) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Map.Entry<String, String>> params = new ArrayList<>();
                    params.add(new AbstractMap.SimpleEntry("country_code", Config.COUNTRY_CODE));
                    params.add(new AbstractMap.SimpleEntry("deviceid", Utility.getDeviceID(getContext())));
                    params.add(new AbstractMap.SimpleEntry("username", StorageHelper.get(StorageHelper.USERNAME)));
                    params.add(new AbstractMap.SimpleEntry("token", StorageHelper.get(StorageHelper.TOKEN)));
                    params.add(new AbstractMap.SimpleEntry("fullname", fullName));
                    params.add(new AbstractMap.SimpleEntry("email", email));
                    params.add(new AbstractMap.SimpleEntry("account_bank", account_bank));
                    params.add(new AbstractMap.SimpleEntry("name_bank", name_bank));
                    params.add(new AbstractMap.SimpleEntry("ctk_bank", ctk_bank));

                    String updateProfileResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_UPFATE_PROFILE, params);
                    final JSONObject jsonObject = new JSONObject(updateProfileResponse);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), getString(R.string.update_info_success), Toast.LENGTH_SHORT).show();
                                new GetProfileApi().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                //Cập nhật username lên left menu
                                TextView txtFullname = getActivity().findViewById(R.id.txt_full_name);
                                txtFullname.setText(fullName);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //endregion

    //region onClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_agree:
//                new UpdateProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,edtShopInfoName.getText().toString(), edtShopInfoEmail.getText().toString(),
//                        edtNumAccBank.getText().toString(),edtBankName.getText().toString(), edtNameAccBank.getText().toString());
                updateprofile(edtShopInfoName.getText().toString(), edtShopInfoEmail.getText().toString(),
                        edtNumAccBank.getText().toString(), edtBankName.getText().toString(), edtNameAccBank.getText().toString());
                break;
            case R.id.iv_back:
//                imgBack.setOnClickListener(null);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                            imgBack.setOnClickListener(ShopInfoFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                getActivity().getSupportFragmentManager().popBackStack();
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HomeFragment.newInstance());
//                CmmFunc.pop(getActivity());
                break;

            case R.id.avatar:
                boolean result = Utility.checkPermission(getContext());
                if (result) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1999);
                }
                break;

            case R.id.txt_change_password:
                CmmFunc.addFragment(getActivity(), R.id.main_container, ChangePasswordDialogFragment.newInstance());
                break;
            case R.id.txt_add_address:
                //truyền params action = 0 để nhận biết tạo address mới
                CmmFunc.addFragment(getActivity(), R.id.main_container, UpdateAddAdressDialogFrragment.newInstance(0));
                break;
        }
    }
    //endregion

    //region Request API

    //region Get Profile
    class GetProfileApi extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.profileAPI(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), StorageHelper.get(StorageHelper.USERNAME),
                        StorageHelper.get(StorageHelper.TOKEN));
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
                    txtFullName.setText(data.getString("fullname"));
                    edtShopInfoCode.setText(data.getString("user_code"));
                    edtShopInfoName.setText(data.getString("fullname"));
                    edtShopInfoEmail.setText(data.getString("email"));
                    edtNumAccBank.setText(data.getString("account_bank"));
                    edtNameAccBank.setText(data.getString("ctk_bank"));
                    edtBankName.setText(data.getString("name_bank"));
                    urlNavHeaderBg = data.getString("avatar");
                    // loading header background image
                    Glide.with(getContext()).load(urlNavHeaderBg)
                            .crossFade().bitmapTransform(new CircleTransform(getContext()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivHeaderPhoto);
                    ivHeaderPhoto.setOnClickListener(ShopInfoFragment.this);
                    shopAddressObjs = (List<ShopAddressObj>) CmmFunc.tryParseList(data.getString("user_address"), ShopAddressObj.class);
                    ShopAddressAdapter shopAddressAdapter = new ShopAddressAdapter(shopAddressObjs, ShopInfoFragment.this, recyclerListAddress);
                    recyclerListAddress.setAdapter(shopAddressAdapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Get Place City
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
                    RegionAdapter regionAdapter = new RegionAdapter(getContext(),
                            (FragmentActivity) getContext(), R.layout.row_spinner_region, regionObjList);
                    spnShopInfoCity.setAdapter(regionAdapter);
                    RegionObj regionObj = RegionObj.getValueByKey(shopCity, regionObjList);
                    int position = regionObjList.indexOf(regionObj);
                    spnShopInfoCity.setSelection(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    //endregion

    //region Get Place District
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
                    RegionAdapter regionAdapter = new RegionAdapter((FragmentActivity) getContext(), (FragmentActivity) getContext(),
                            R.layout.row_spinner_region, regionObjList);
                    spnShopInfoCounty.setAdapter(regionAdapter);
                    RegionObj regionObj = RegionObj.getValueByKey(shopDistrict, regionObjList);
                    int position = regionObjList.indexOf(regionObj);
                    spnShopInfoCounty.setSelection(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Get Place Ward
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
                    RegionAdapter regionAdapter = new RegionAdapter((FragmentActivity) getContext(), (FragmentActivity) getContext(), R.layout.row_spinner_region, regionObjList);
                    spnShopInfoWard.setAdapter(regionAdapter);
                    RegionObj regionObj = RegionObj.getValueByKey(shopWard, regionObjList);
                    int position = regionObjList.indexOf(regionObj);
                    spnShopInfoWard.setSelection(position);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion
}

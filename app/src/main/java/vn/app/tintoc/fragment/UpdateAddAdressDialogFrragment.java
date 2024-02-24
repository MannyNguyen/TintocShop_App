package vn.app.tintoc.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import vn.app.tintoc.R;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.Utility;

/**
 * Created by IPP on 1/22/2018.
 */

public class UpdateAddAdressDialogFrragment extends BaseFragment implements View.OnClickListener {
    TextView txtAddressNumber, txtShopRegion;
    EditText edtShopName, edtShopPhone, edtShopAddress;
    Button btnCancel, btnSend;
    FrameLayout frameEditAddress;
    int idCity = 0;
    int idDistrict = 0;
    int idWard = 0;

    public static UpdateAddAdressDialogFrragment newInstance(int action) {
        Bundle args = new Bundle();
        args.putInt("action", action);
        UpdateAddAdressDialogFrragment fragment = new UpdateAddAdressDialogFrragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static UpdateAddAdressDialogFrragment newInstance(int addressId, String shopName, String shopPhone, String shopAddress,
                                                             String shopRegion, int city_id, int district_id, int ward_id, int action) {
        Bundle args = new Bundle();
        args.putInt("addressId", addressId);
        args.putString("shopName", shopName);
        args.putString("shopPhone", shopPhone);
        args.putString("shopAddress", shopAddress);
        args.putString("shopRegion", shopRegion);
        args.putInt("city_id", city_id);
        args.putInt("district_id", district_id);
        args.putInt("ward_id", ward_id);
        args.putInt("action", action);
        UpdateAddAdressDialogFrragment fragment = new UpdateAddAdressDialogFrragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view != null){
            return view;
        }
        view = inflater.inflate(R.layout.update_add_address_dialogfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            txtAddressNumber = view.findViewById(R.id.txt_address_number);
            txtShopRegion = view.findViewById(R.id.txt_shop_region);
            edtShopName = view.findViewById(R.id.edt_shop_name);
            edtShopPhone = view.findViewById(R.id.edt_shop_phone);
            edtShopAddress = view.findViewById(R.id.edt_shop_address);
            btnCancel = view.findViewById(R.id.btn_cancel);
            btnSend = view.findViewById(R.id.btn_send);
            frameEditAddress=view.findViewById(R.id.frame_edit_address);
            btnCancel.setOnClickListener(this);
            btnSend.setOnClickListener(this);
            txtShopRegion.setOnClickListener(this);
            RegionDialogFragment.regionAddressList.clear();

            txtAddressNumber.setText(R.string.main_address + getArguments().getInt("addressId"));
            edtShopName.setText(getArguments().getString("shopName"));
            edtShopPhone.setText(getArguments().getString("shopPhone"));
            edtShopAddress.setText(getArguments().getString("shopAddress"));
            txtShopRegion.setText(getArguments().getString("shopRegion"));

            idCity = getArguments().getInt("city_id");
            idDistrict = getArguments().getInt("district_id");
            idWard = getArguments().getInt("ward_id");

            frameEditAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            isLoad = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (RegionDialogFragment.regionAddressList.size() != 3) {
            RegionDialogFragment.regionAddressList.clear();
            if (txtShopRegion == null) {
                return;
            }
            txtShopRegion.setText(getArguments().getString("shopRegion"));
            return;
        }
        String address = StringUtils.EMPTY;
        idCity = 0;
        idDistrict = 0;
        idWard = 0;

        //Lấy tên tỉnh thành theo: Thành phố - Phường - Quận
        for (int i = 0; i < RegionDialogFragment.regionAddressList.size(); i++) {
            address += RegionDialogFragment.regionAddressList.get(i).getName() + " - ";
        }
        //For lấy tên tỉnh thành theo: Phường - Quận - Thành phố
//        for (int i = RegionDialogFragment.regionObjList.size() - 1; i >= 0; i--) {
//            address += RegionDialogFragment.regionObjList.get(i).getName() + " - ";
//        }
        address = address.substring(0, address.length() - 3);
        idCity = RegionDialogFragment.regionAddressList.get(0).getId();
        idDistrict = RegionDialogFragment.regionAddressList.get(1).getId();
        idWard = RegionDialogFragment.regionAddressList.get(2).getId();
        txtShopRegion.setText(address);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                btnCancel.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            btnCancel.setOnClickListener(UpdateAddAdressDialogFrragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.txt_shop_region:
                RegionDialogFragment.regionAddressList.clear();
                CmmFunc.addFragment(getActivity(), R.id.main_container, RegionDialogFragment.newInstance(3, 0, 0, 0));
                txtShopRegion.setError(null);
                hideKeyboard();
                break;
            case R.id.btn_send:
                if (edtShopName.getText().toString().equals("")) {
                    edtShopName.setError(getString(R.string.err_shop_name));
                    edtShopName.requestFocus();
                    return;
                }
                if (edtShopPhone.getText().toString().equals("")) {
                    edtShopPhone.setError(getString(R.string.err_phone_num));
                    edtShopPhone.requestFocus();
                    return;
                }
                if (edtShopAddress.getText().toString().equals("")) {
                    edtShopAddress.setError(getString(R.string.err_address));
                    edtShopAddress.requestFocus();
                    return;
                }
                if (txtShopRegion.getText().toString().equals("")) {
                    txtShopRegion.requestFocus();
                    Toast.makeText(getContext(), getString(R.string.err_region), Toast.LENGTH_SHORT).show();
                    return;
                }
                new GetSubmit().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, edtShopPhone.getText().toString(), edtShopName.getText().toString(), edtShopAddress.getText().toString(),
                        idCity, idDistrict, idWard, getArguments().getInt("action"), getArguments().getInt("addressId"));
                break;
        }
    }

    //region request API
    class GetSubmit extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String phone = (String) objects[0];
                String name = (String) objects[1];
                String address = (String) objects[2];
                int city_id = (int) objects[3];
                int district_id = (int) objects[4];
                int ward_id = (int) objects[5];
                int action = (int) objects[6];
                int id_address = (int) objects[7];
                String value = APIHelper.getSubmitAddress(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), StorageHelper.get(StorageHelper.USERNAME),
                        StorageHelper.get(StorageHelper.TOKEN), phone, name, address, city_id, district_id, ward_id, action, id_address);
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
                    if (getArguments().getInt("action") == 1) {
                        Toast.makeText(getContext(), R.string.update_address_success, Toast.LENGTH_SHORT).show();
                        CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                    }
                    if (getArguments().getInt("action") == 0) {
                        Toast.makeText(getContext(), R.string.add_address_success, Toast.LENGTH_SHORT).show();
                        CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion
}

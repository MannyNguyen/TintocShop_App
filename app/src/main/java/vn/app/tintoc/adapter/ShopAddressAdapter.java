package vn.app.tintoc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.app.tintoc.R;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.fragment.CreateOrderFragment;
import vn.app.tintoc.fragment.ShopInfoFragment;
import vn.app.tintoc.fragment.UpdateAddAdressDialogFrragment;
import vn.app.tintoc.model.ShopAddressObj;

import static vn.app.tintoc.config.GlobalClass.getActivity;

/**
 * Created by IPP on 1/22/2018.
 */

public class ShopAddressAdapter extends RecyclerView.Adapter<ShopAddressAdapter.MyViewHolder> implements View.OnClickListener {
    List<ShopAddressObj> shopAddressObjs;
    ShopInfoFragment fragment;
    RecyclerView recyclerView;
    int type = 0;
    int idCity = 0;
    int idDistrict = 0;
    int idWard = 0;

    public ShopAddressAdapter(List<ShopAddressObj> shopAddressObjs, ShopInfoFragment fragment, RecyclerView recyclerView) {
        this.shopAddressObjs = shopAddressObjs;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_address, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            final ShopAddressObj shopAddressObj = shopAddressObjs.get(position);
            holder.txtNumAddress.setText("Địa chỉ " + (position + 1));
            holder.txtInfoSenderName.setText(shopAddressObj.getName());
            holder.txtInfoPhoneNum.setText(shopAddressObj.getPhone());
            holder.txtInfoAddress.setText(shopAddressObj.getAddress());
            final String shopRegion;
            if (shopAddressObj.getWard_name().equals("")) {
                shopRegion = shopAddressObj.getCity_name() + " - " + shopAddressObj.getDistrict_name();
            } else {
                shopRegion = shopAddressObj.getCity_name() + " - " + shopAddressObj.getDistrict_name() + " - " + shopAddressObj.getWard_name();
            }
            holder.txtInfoRegion.setText(shopRegion);

            if (type == 0) {
                holder.txtEditAddress.setVisibility(View.VISIBLE);
                holder.txtChooseAddress.setVisibility(View.GONE);
                holder.txtEditAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CmmFunc.addFragment(getActivity(), R.id.main_container, UpdateAddAdressDialogFrragment.newInstance(shopAddressObj.getId_address(), shopAddressObj.getName(),
                                shopAddressObj.getPhone(), shopAddressObj.getAddress(), shopRegion, shopAddressObj.getCity_id(), shopAddressObj.getDistrict_id(), shopAddressObj.getWard_id(), 1));
                    }
                });
            }
            if (type == 1) {
                holder.txtEditAddress.setVisibility(View.GONE);
                holder.txtChooseAddress.setVisibility(View.VISIBLE);
                holder.txtChooseAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return shopAddressObjs.size();
    }

    @Override
    public void onClick(View view) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumAddress, txtInfoSenderName, txtInfoPhoneNum, txtInfoAddress, txtInfoRegion, txtEditAddress, txtChooseAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtNumAddress = itemView.findViewById(R.id.txt_num_address);
            txtEditAddress = itemView.findViewById(R.id.txt_edit_address);
            txtInfoSenderName = itemView.findViewById(R.id.txt_info_sender_name);
            txtInfoPhoneNum = itemView.findViewById(R.id.txt_info_phone_num);
            txtInfoAddress = itemView.findViewById(R.id.txt_info_address);
            txtInfoRegion = itemView.findViewById(R.id.txt_info_region);
            txtChooseAddress = itemView.findViewById(R.id.txt_choose_address);
        }
    }
}

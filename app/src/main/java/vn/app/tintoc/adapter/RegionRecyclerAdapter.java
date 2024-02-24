package vn.app.tintoc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.fragment.CreateOrderFragment;
import vn.app.tintoc.fragment.RegionDialogFragment;
import vn.app.tintoc.fragment.UpdateAddAdressDialogFrragment;
import vn.app.tintoc.model.RegionObj;

import java.util.List;

/**
 * Created by Admin on 10/18/2017.
 */

public class RegionRecyclerAdapter extends RecyclerView.Adapter<RegionRecyclerAdapter.MyViewHolder> implements View.OnClickListener {
    List<RegionObj> items;
    List<RegionObj> regionObjList;
    RegionDialogFragment fragment;
    RecyclerView recyclerView;
    String city, district, ward;
    private int type;


    /**
     * @param type
     * @param items         List tỉnh thành từ server
     * @param regionObjList Sau khi chọn tỉnh thành, add vào list này
     * @param fragment
     * @param recyclerView
     */
    public RegionRecyclerAdapter(int type, List<RegionObj> items, List<RegionObj> regionObjList, RegionDialogFragment fragment, RecyclerView recyclerView) {
        this.items = items;
        this.regionObjList = regionObjList;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_spinner_region, parent, false);
        itemView.setOnClickListener(RegionRecyclerAdapter.this);
        return new RegionRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            RegionObj regionObj = items.get(position);
            holder.txtRegion.setText(regionObj.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        RegionObj item = items.get(position);
        if (item != null) {
            regionObjList.add(item);
            //Fragment dialog chọn tỉnh thành sau khi chọn quận/huyện, mở recycler chọn phường/xã, sau khi chọn thì vào onResume() của Fragment để set data.
            if (fragment.getArguments().getInt("idDistrict") > 0) {
                //Sau khi hoàn tất việc chọn tỉnh thành load lại dialog update địa chỉ shop
                //return mỗi func, tránh trường hợp nhảy nhầm func
                //Màn hình Shop info, update/add địa chỉ
                UpdateAddAdressDialogFrragment updateAddAdressDialogFrragment = (UpdateAddAdressDialogFrragment) fragment.getActivity()
                        .getSupportFragmentManager().findFragmentByTag(UpdateAddAdressDialogFrragment.class.getName());
                if (updateAddAdressDialogFrragment != null) {
                    try {
                        fragment.getActivity().getSupportFragmentManager().popBackStackImmediate(UpdateAddAdressDialogFrragment.class.getName(), 0);
                        updateAddAdressDialogFrragment.onResume();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }

                //Sau khi hoàn thành việc chọn tỉnh thành, sẽ load lại toàn bộ màn hình tạo đơn hàng vì phương thức addFragment vẫn giữa data cũ của màn hình tạo đơn hàng.
                //Màn hình tạo đơn hàng
                CreateOrderFragment createOrderFragment = (CreateOrderFragment) fragment.getActivity().getSupportFragmentManager().findFragmentByTag(CreateOrderFragment.class.getName());
                if (createOrderFragment != null) {
                    try {
                        fragment.getActivity().getSupportFragmentManager().
                                popBackStackImmediate(CreateOrderFragment.class.getName(), 0);
                        createOrderFragment.onResume();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            //Fragment dialog chọn tỉnh thành sau khi đã chọn thành phố, mở reccyler với data quận/huyện
            if (fragment.getArguments().getInt("idCity") > 0) {
                CmmFunc.addFragment(fragment.getActivity(), R.id.main_container,
                        RegionDialogFragment.newInstance(type, fragment.getArguments().getInt("idCity"),
                                item.getId(), 0));
                return;
            }
            //Fragment dialog chọn tỉnh thành luc khởi tạo, không có idCity, idDistrict, idWard
            CmmFunc.addFragment(fragment.getActivity(), R.id.main_container,
                    RegionDialogFragment.newInstance(type, item.getId(),
                            0, 0));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtRegion;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtRegion = itemView.findViewById(R.id.txt_region_name);
        }
    }
}

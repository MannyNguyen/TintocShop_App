package vn.app.tintoc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.app.tintoc.R;
import vn.app.tintoc.fragment.CreateOrderFragment;
import vn.app.tintoc.model.RegionObj;
import vn.app.tintoc.model.ShopAddressObj;

/**
 * Created by IPP on 1/24/2018.
 */

public class AddressCreateOrderAdapter extends ArrayAdapter {
    FragmentActivity activity;
    List<ShopAddressObj> items;
    CreateOrderFragment fragment;

    //region Constructor
    public AddressCreateOrderAdapter(Context context, FragmentActivity activity, int textViewResourceId,CreateOrderFragment fragment, List<ShopAddressObj> items) {
        super(context, textViewResourceId, items);
        this.activity = activity;
        this.fragment=fragment;
        this.items = items;
    }
    //endregion

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.row_spinner_region, parent, false);
        TextView txtAddress = layout.findViewById(R.id.txt_region_name);
        txtAddress.setText(items.get(position).getName());
        return layout;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View layout = getCustomView(position, convertView, parent);
        TextView txtAddress = layout.findViewById(R.id.txt_region_name);
        txtAddress.setText(items.get(position).getName());
        return layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}

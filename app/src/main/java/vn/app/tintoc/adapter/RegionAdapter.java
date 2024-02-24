package vn.app.tintoc.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.model.RegionObj;

import java.util.List;

/**
 * Created by Admin on 7/30/2017.
 */

public class RegionAdapter extends ArrayAdapter {

    //region Var
    FragmentActivity activity;
    List<RegionObj> items;
    //endregion

    //region Constructor
    public RegionAdapter(Context context, FragmentActivity activity, int textViewResourceId,
                         List<RegionObj> items) {
        super(context, textViewResourceId, items);
        this.activity = activity;
        this.items = items;
    }
    //endregion

    //region GetCustomView
    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.row_spinner_region, parent, false);
        TextView txtRegion = layout.findViewById(R.id.txt_region_name);
        txtRegion.setText(items.get(position).getName());
        return layout;
    }
    //endregion

    //region GetDropdownView
    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    //endregion

    //region GetView
    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    //endregion
}

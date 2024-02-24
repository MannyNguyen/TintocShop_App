package vn.app.tintoc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.model.AddOnServiceObj;

import java.util.List;

/**
 * Created by Admin on 9/19/2017.
 */

public class AddOnServiceAdapter extends ArrayAdapter {


    //region Var
    FragmentActivity activity;
    List<AddOnServiceObj> items;
    int add_on_protect;
    //endregion

    //region Constructor
    public AddOnServiceAdapter(@NonNull Context context, FragmentActivity activity, int textViewResourceId,
                               List<AddOnServiceObj> items, int add_on_protect) {
        super(context, textViewResourceId, items);
        this.activity = activity;
        this.items = items;
        this.add_on_protect = add_on_protect;
    }
    //endregion

    //region getCustomView
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        int id = items.get(position).getId();
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.row_spinner_addon_service, parent, false);
        TextView txtAddon = layout.findViewById(R.id.txt_addon);
        txtAddon.setText(items.get(position).getName());


        return layout;
    }
    //endregion

    //region Get drop down view
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = getCustomView(position, convertView, parent);
        TextView textView = (TextView) v.findViewById(R.id.txt_addon);
        try {
            if (items.get(position).isActive()) {
                textView.setTextColor(activity.getResources().getColor(R.color.txt_title));
            } else {
                textView.setTextColor(activity.getResources().getColor(android.R.color.darker_gray));
                v.setOnClickListener(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }
    //endregion

    //region Get view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    //endregion
}

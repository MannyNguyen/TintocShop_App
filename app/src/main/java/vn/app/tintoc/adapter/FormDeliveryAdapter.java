package vn.app.tintoc.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.model.FormDeliveryObj;

import java.util.List;

/**
 * Created by Admin on 7/31/2017.
 */

public class FormDeliveryAdapter extends ArrayAdapter {

    //region Var
    FragmentActivity activity;
    public List<FormDeliveryObj> items;
    //endregion

    //region Constructor
    public FormDeliveryAdapter(Context context, FragmentActivity activity, int textViewResourceId,
                               List<FormDeliveryObj> items) {
        super(context, textViewResourceId, items);
        this.activity = activity;
        this.items = items;
    }
    //endregion

    //region GetCustomView
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.row_spinner_form_delivery, parent, false);
        TextView txtFormDelivery = layout.findViewById(R.id.txt_form_delivery);
        txtFormDelivery.setText(items.get(position).getForm_deliver_name());
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

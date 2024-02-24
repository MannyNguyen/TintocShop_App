package vn.app.tintoc.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.model.WeightProductObj;

import java.util.List;

/**
 * Created by Admin on 7/31/2017.
 */

public class WeightProductAdapter extends ArrayAdapter {

    //region Var
    FragmentActivity activity;
    List<WeightProductObj> items;
    //endregion

    //region Constructor
    public WeightProductAdapter(Context context, FragmentActivity activity, int textViewResourceId,
                                List<WeightProductObj> items) {
        super(context, textViewResourceId, items);
        this.activity = activity;
        this.items = items;
    }
    //endregion

    //region GetCustomView
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.row_spinner_weight_product, parent, false);
        TextView txtWeightProduct = layout.findViewById(R.id.txt_weight_product);
        txtWeightProduct.setText(items.get(position).getWeight());
        return layout;
    }
    //endregion

    //region GetDropdownView
    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
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

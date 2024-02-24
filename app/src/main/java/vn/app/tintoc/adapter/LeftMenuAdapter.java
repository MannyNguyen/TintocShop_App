package vn.app.tintoc.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang.StringUtils;

import vn.app.tintoc.LoginActivity;
import vn.app.tintoc.MainActivity;
import vn.app.tintoc.R;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.GlobalClass;
import vn.app.tintoc.fragment.IncentiveFragment;
import vn.app.tintoc.fragment.IncomeFragment;
import vn.app.tintoc.fragment.LoginFragment;
import vn.app.tintoc.fragment.RatingFragment;
import vn.app.tintoc.fragment.ShopInfoFragment;
import vn.app.tintoc.fragment.SupportFragment;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.LeftMenu;

import java.util.List;

import static vn.app.tintoc.config.GlobalClass.getActivity;

/**
 * Created by Admin on 7/31/2017.
 */

public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.MyViewHolder> implements View.OnClickListener {

    //region Var
    public List<LeftMenu> leftMenuList;
    RecyclerView recyclerView;
    //endregion

    //region Constructor
    public LeftMenuAdapter(RecyclerView recyclerView, List<LeftMenu> leftMenuList) {
        this.recyclerView = recyclerView;
        this.leftMenuList = leftMenuList;
    }
    //endregion

    //region OnCreateViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_left_menu, parent, false);
        itemView.setOnClickListener(LeftMenuAdapter.this);
        return new MyViewHolder(itemView);
    }
    //endregion

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            LeftMenu leftMenu = leftMenuList.get(position);
            holder.txtTextMenu.setText(leftMenu.getTextMenu());
//            holder.txtNumNotify.setText(leftMenu.getNumNotify());
            holder.ivIconMenu.setImageResource(leftMenu.getIvIconMenu());
            if (leftMenu.getType() == LeftMenu.INCOME) {
                if (leftMenu.isShowNotify()) {
                    holder.ivNotify.setVisibility(View.VISIBLE);
                } else {
                    holder.ivNotify.setVisibility(View.GONE);
                }
            } else {
                holder.ivNotify.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region GetItemCount
    @Override
    public int getItemCount() {
        return leftMenuList.size();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        LeftMenu item = leftMenuList.get(position);
        if (item != null) {
            switch (item.getType()) {
                case LeftMenu.SHOP_INFO:
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                    break;
                case LeftMenu.INCOME:
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, IncomeFragment.newInstance());
                    break;
                case LeftMenu.INCENTIVE:
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, IncentiveFragment.newInstance());
                    break;
                case LeftMenu.RATING:
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, RatingFragment.newInstance());
                    break;
                case LeftMenu.SUPPORT:
                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, SupportFragment.newInstance());
                    break;
                case LeftMenu.LOGOUT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.logout_title);
                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            StorageHelper.set(StorageHelper.TOKEN, StringUtils.EMPTY);
                            StorageHelper.set(StorageHelper.USERNAME, StringUtils.EMPTY);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
            }
        }
    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTextMenu, txtNumNotify;
        public ImageView ivIconMenu, ivNotify;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTextMenu = itemView.findViewById(R.id.txt_text_menu);
//            txtNumNotify = itemView.findViewById(R.id.txt_num_notify);
            ivNotify = itemView.findViewById(R.id.iv_notify);
            ivIconMenu = itemView.findViewById(R.id.iv_item_menu);
        }
    }
    //endregion
}

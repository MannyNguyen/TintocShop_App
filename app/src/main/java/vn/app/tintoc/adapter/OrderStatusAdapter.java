package vn.app.tintoc.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.fragment.ShopOrderDetailFragment;
import vn.app.tintoc.model.OrderStatusObj;

import java.util.List;

/**
 * Created by Admin on 9/25/2017.
 */

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.MyViewHolder> {
    //region Var
    FragmentActivity activity;
    List<OrderStatusObj> orderStatusObjList;
    ShopOrderDetailFragment shopOrderDetailFragment;
    RecyclerView recyclerView;
    //endregion

    //region Constructor
    public OrderStatusAdapter(ShopOrderDetailFragment shopOrderDetailFragment, RecyclerView recyclerView,
                              List<OrderStatusObj> orderStatusObjList, int statusId) {
        this.shopOrderDetailFragment = shopOrderDetailFragment;
        this.recyclerView = recyclerView;
        this.orderStatusObjList = orderStatusObjList;
        OrderStatusObj.updateStatus(statusId, this.orderStatusObjList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_status, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            OrderStatusObj orderStatusObj = orderStatusObjList.get(position);
            holder.txtTitle.setText(orderStatusObj.getStatus_name());
            holder.txtTitle.setTextColor(shopOrderDetailFragment.getResources().getColor(R.color.gray_transparent_main));

            holder.startLine.setVisibility(View.VISIBLE);
            holder.endLine.setVisibility(View.VISIBLE);
            holder.startLine.setBackgroundResource(R.color.gray_transparent_light_main);
            holder.endLine.setBackgroundResource(R.color.gray_transparent_light_main);

            if (position == 0) {
                holder.startLine.setVisibility(View.INVISIBLE);
            }
            if (position == orderStatusObjList.size() - 1) {
                holder.endLine.setVisibility(View.INVISIBLE);
            }


            switch (orderStatusObj.getIsCheck()) {
                case OrderStatusObj.DONE:
                    holder.ivCheck.setImageResource(R.drawable.slice_2);
                    holder.startLine.setBackgroundResource(R.color.colorShopInfoTwo);
                    holder.endLine.setBackgroundResource(R.color.colorShopInfoTwo);
                    break;
                case OrderStatusObj.IN_PROGRESS:
                    holder.ivCheck.setImageResource(R.drawable.slice_1);
                    holder.startLine.setBackgroundResource(R.color.colorShopInfoTwo);
                    holder.endLine.setBackgroundResource(R.color.gray_transparent_light_main);
                    holder.txtTitle.setTextColor(shopOrderDetailFragment.getResources().getColor(R.color.colorShopInfoTwo));
                    break;
                case OrderStatusObj.WAITING:
                    holder.ivCheck.setImageResource(R.drawable.slice_3);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderStatusObjList.size();
    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView ivCheck;
        View startLine, endLine;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.title);
            ivCheck = itemView.findViewById(R.id.check);
            startLine = itemView.findViewById(R.id.start_line);
            endLine = itemView.findViewById(R.id.end_line);
        }
    }
    //endregion
}

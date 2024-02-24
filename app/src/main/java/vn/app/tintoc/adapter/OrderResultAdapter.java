package vn.app.tintoc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.fragment.HomeFragment;
import vn.app.tintoc.fragment.ShopOrderDetailFragment;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.OrderResultObj;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static vn.app.tintoc.config.GlobalClass.getActivity;

/**
 * Created by Admin on 8/7/2017.
 */

public class OrderResultAdapter extends RecyclerView.Adapter<OrderResultAdapter.MyViewHolder> implements View.OnClickListener {

    //region Var
    List<OrderResultObj> orderResultObjList;
    HomeFragment fragment;
    RecyclerView recyclerView;
    private String orderCode;
    //endregion

    //region Constructor
    public OrderResultAdapter(HomeFragment fragment, RecyclerView recyclerView, List<OrderResultObj> orderResultObjList) {
        this.fragment = fragment;
        this.recyclerView = recyclerView;
        this.orderResultObjList = orderResultObjList;
    }
    //endregion

    //region OnCreateViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_screen, parent, false);
        itemView.setOnClickListener(OrderResultAdapter.this);
        return new MyViewHolder(itemView);
    }
    //endregion

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            OrderResultObj orderResultObj = orderResultObjList.get(position);
            holder.txtOrderCode.setText(orderResultObj.getOrder_code());
            holder.txtCreateDate.setText(orderResultObj.getCreate_date());
            holder.txtFullName.setText(orderResultObj.getReceiver_fullname());
            holder.txtAddress.setText("Địa chỉ: " + orderResultObj.getAddress());
            holder.txtTotal.setText(CmmFunc.formatMoney(Math.round(orderResultObj.getTotal())) + " VND");
            holder.txtOrderStatus.setText(orderResultObj.getStatus());
            if (orderResultObj.getShop_order_code().equals("")){
                holder.txtShopCode.setVisibility(View.GONE);
                holder.shopOrderCode.setVisibility(View.GONE);
            }else {
                holder.txtShopCode.setVisibility(View.VISIBLE);
                holder.shopOrderCode.setText(orderResultObj.getShop_order_code());
            }

            holder.txtTimeUpdateStatus.setText(orderResultObj.getTime_update_status());
            if (orderResultObj.getPayment_status() == 0) {
                holder.txtPaymentStatus.setText(R.string.not_payment);
            }
            if (orderResultObj.getPayment_status() == 1) {
                holder.txtPaymentStatus.setText(R.string.compared);
            }
            if (orderResultObj.getPayment_status() == 2) {
                holder.txtPaymentStatus.setText(R.string.product_payed);
            }


//            DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//            String dateReceiver = orderResultObj.getDate_delivery();
//            String dateReceiverFormat = dateReceiver.substring(0, dateReceiver.length() - 2);
//            DateTime dateTime = DateTime.parse(dateReceiverFormat, df);
//            String value = formatDate(dateTime);
//            holder.txtDateReceiver.setText(value);


//            holder.txtNumerical.setText((position + 1) + "");
            int value = numOfOrder();
            if (value > 0) {
                fragment.llNumOrder.setVisibility(View.VISIBLE);
                fragment.txtNumOrder.setText(value + " đơn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region GetItemCount
    @Override
    public int getItemCount() {
        return orderResultObjList.size();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        OrderResultObj item = orderResultObjList.get(position);
        if (item != null) {
            orderCode = item.getOrder_code();
            CmmFunc.addFragment(getActivity(), R.id.main_container, ShopOrderDetailFragment.newInstance(orderCode, StorageHelper.get(StorageHelper.USERNAME),
                    StorageHelper.get(StorageHelper.TOKEN), false));
        }
    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderCode, txtCreateDate, txtOrderStatus, txtFullName, txtAddress, txtTotal, txtTimeUpdateStatus, txtPaymentStatus, shopOrderCode, txtShopCode;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtOrderCode = itemView.findViewById(R.id.txt_number_code);
            txtCreateDate = itemView.findViewById(R.id.txt_create_date);
            txtOrderStatus = itemView.findViewById(R.id.txt_filter_status);
            txtFullName = itemView.findViewById(R.id.txt_full_name);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtTotal = itemView.findViewById(R.id.txt_total);
            txtTimeUpdateStatus = itemView.findViewById(R.id.txt_time_update_status);
            txtPaymentStatus = itemView.findViewById(R.id.txt_payment_status);
            shopOrderCode = itemView.findViewById(R.id.shop_order_code);
            txtShopCode=itemView.findViewById(R.id.txt_shop_code);
        }
    }
    //endregion

    //region Methods
    private String formatDate(DateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        String retValue = StringUtils.leftPad(dateTime.getDayOfMonth() + "", 2, "0") + "-" + StringUtils.leftPad(dateTime.getMonthOfYear() + "", 2, "0") + "-" + dateTime.getYear();
        return retValue;
    }

    private int numOfOrder() {
        int value = 0;
        for (OrderResultObj orderResultObj : orderResultObjList) {
            value++;
        }
        return value;
    }
    //endregion
}

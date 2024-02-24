package vn.app.tintoc.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.app.tintoc.R;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.fragment.IncomeFragment;
import vn.app.tintoc.model.IncomeObj;

import java.util.List;

/**
 * Created by Admin on 9/27/2017.
 */

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.MyViewHolder> implements View.OnClickListener {
    //region Var
    List<IncomeObj> incomeObjList;
    IncomeFragment fragment;
    RecyclerView recyclerView;
    int type;
    //endregion

    //region Constructor
    public IncomeAdapter(IncomeFragment fragment, RecyclerView recyclerView, List<IncomeObj> incomeObjList, int type) {
        this.fragment = fragment;
        this.recyclerView = recyclerView;
        this.incomeObjList = incomeObjList;
        this.type = type;
    }
    //endregion

    //region OnCreateViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income_screen, parent, false);
        itemView.setOnClickListener(IncomeAdapter.this);
        return new MyViewHolder(itemView);
    }
    //endregion

    //region OnBindViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            IncomeObj incomeObj = incomeObjList.get(position);
            holder.txtStt.setText((position + 1) + "");
            String orderCode = incomeObj.getOrder_code();
            String fullName = incomeObj.getFullname();
            holder.txtCodeName.setText(orderCode + " - " + fullName);
            holder.txtAddress.setText("Địa chỉ: " + incomeObj.getAddress());
            holder.txtTotalIncome.setText(CmmFunc.formatMoney(Math.round(Float.parseFloat(incomeObj.getTotal_cod()))) + "đ");
            holder.txtTotalFee.setText(CmmFunc.formatMoney(Math.round(Float.parseFloat(incomeObj.getTotal_fee()))) + "đ");
            holder.txtIncome.setText(CmmFunc.formatMoney(Math.round(Float.parseFloat(incomeObj.getTotal_shop()))) + "đ");
            holder.txtMethod.setText(incomeObj.getPayment_type());

//            int numOrder = incomeObjList.size();
            int value = numOrder();
            if (value > 0) {
                fragment.llNumIncomeOrder.setVisibility(View.VISIBLE);
                fragment.txtNumberOrder.setText(value + " đơn");
            }

            if (type == 2) {
                holder.txtMethod.setVisibility(View.GONE);
//                holder.txtMethod.setText((position + 1) + "");
            } else {
                holder.txtMethod.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region GetItemCount
    @Override
    public int getItemCount() {
        return incomeObjList.size();
    }
    //endregion

    //region count order
    private int numOrder() {
        int value = 0;
        for (IncomeObj incomeObj : incomeObjList) {
            value++;
        }
        return value;
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
//        int position = recyclerView.getChildLayoutPosition(view);
//        IncomeObj item = incomeObjList.get(position);
//        if (item != null) {
//            ShopOrderDetailFragment shopOrderDetailFragment = ShopOrderDetailFragment.newInstance(code, userName, token, false);
//            CmmFunc.replaceFragment(getActivity(), R.id.main_container, shopOrderDetailFragment);
//        }
    }
    //endregion

    //region MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStt, txtCodeName, txtAddress, txtTotalIncome, txtTotalFee, txtIncome, txtMethod;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtStt = itemView.findViewById(R.id.txt_stt);
//            txtCode = itemView.findViewById(R.id.txt_code);
            txtCodeName = itemView.findViewById(R.id.txt_code_name);
            txtAddress = itemView.findViewById(R.id.txt_address);
            txtTotalIncome = itemView.findViewById(R.id.txt_total_income);
            txtTotalFee = itemView.findViewById(R.id.txt_fee_total);
            txtIncome = itemView.findViewById(R.id.txt_income);
            txtMethod = itemView.findViewById(R.id.txt_method);
        }
    }
    //endregion
}

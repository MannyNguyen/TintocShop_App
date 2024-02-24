package vn.app.tintoc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.app.tintoc.R;
import vn.app.tintoc.fragment.HistoryDialogFragment;
import vn.app.tintoc.model.HistoryObj;

/**
 * Created by IPP on 1/24/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> implements View.OnClickListener {
    List<HistoryObj> historyObjs;
    HistoryDialogFragment fragment;
    RecyclerView recyclerView;

    public HistoryAdapter(List<HistoryObj> historyObjs, HistoryDialogFragment fragment, RecyclerView recyclerView) {
        this.historyObjs = historyObjs;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_order, parent, false);
        itemView.setOnClickListener(HistoryAdapter.this);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            HistoryObj historyObj = historyObjs.get(position);
            holder.txtStt.setText(historyObj.getId()+"");
            holder.txtContent.setText(historyObj.getNote());
            holder.txtTime.setText(historyObj.getTime_confirm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return historyObjs.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStt, txtContent, txtTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtStt = itemView.findViewById(R.id.txt_history_stt);
            txtContent = itemView.findViewById(R.id.txt_history_content);
            txtTime = itemView.findViewById(R.id.txt_history_time);
        }
    }
}

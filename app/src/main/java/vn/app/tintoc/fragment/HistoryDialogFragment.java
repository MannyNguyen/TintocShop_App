package vn.app.tintoc.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import vn.app.tintoc.R;
import vn.app.tintoc.adapter.HistoryAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.HistoryObj;
import vn.app.tintoc.utils.Utility;

/**
 * Created by IPP on 1/24/2018.
 */

public class HistoryDialogFragment extends BaseFragment implements View.OnClickListener {
    ImageView ivClose;
    TextView txtHistoryOrderCode;
    public TextView txtNoHistory;
    FrameLayout frameHistory;
    RecyclerView recyclerHistory;
    List<HistoryObj> historyObjs;

    public static HistoryDialogFragment newInstance(String orderCode) {
        Bundle args = new Bundle();
        args.putString("orderCode", orderCode);
        HistoryDialogFragment fragment = new HistoryDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.history_dialogfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivClose = view.findViewById(R.id.iv_close);
            txtHistoryOrderCode = view.findViewById(R.id.txt_history_order_code);
            txtNoHistory = view.findViewById(R.id.txt_no_history);
            frameHistory = view.findViewById(R.id.frame_history);
            recyclerHistory = view.findViewById(R.id.recycler_history);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerHistory.setLayoutManager(layoutManager);

            txtHistoryOrderCode.setText("(" + getArguments().getString("orderCode") + ")");

            ivClose.setOnClickListener(this);
            frameHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            new GetHistory().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getArguments().getString("orderCode"));

            isLoad = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                ivClose.setOnClickListener(null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);

                            ivClose.setOnClickListener(HistoryDialogFragment.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    //region Request API
    class GetHistory extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String orderCode = (String) objects[0];
                String value = APIHelper.getHistory(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), StorageHelper.get(StorageHelper.USERNAME),
                        StorageHelper.get(StorageHelper.TOKEN), orderCode);
                jsonObject = new JSONObject(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    historyObjs = (List<HistoryObj>) CmmFunc.tryParseList(jsonObject.getString("data"), HistoryObj.class);
                    HistoryAdapter historyAdapter = new HistoryAdapter(historyObjs, HistoryDialogFragment.this, recyclerHistory);
                    recyclerHistory.setAdapter(historyAdapter);
                    if (historyObjs.isEmpty()) {
                        txtNoHistory.setVisibility(View.VISIBLE);
                        recyclerHistory.setVisibility(View.GONE);
                    } else {
                        txtNoHistory.setVisibility(View.GONE);
                        recyclerHistory.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion
}

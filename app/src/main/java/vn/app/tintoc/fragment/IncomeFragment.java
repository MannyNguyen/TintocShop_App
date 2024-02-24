package vn.app.tintoc.fragment;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import vn.app.tintoc.R;
import vn.app.tintoc.adapter.IncomeAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.IncomeObj;
import vn.app.tintoc.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    FrameLayout llPayed, llNotPayed, llCompared;
    TextView txtIncomeFromDate, txtIncomeToDate, txtType;
    public LinearLayout llNumIncomeOrder;
    public TextView txtNumberOrder;
    ImageView ivBack, ivAlert1, ivAlert2, ivBackground;
    private RecyclerView recyclerListIncome;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<IncomeObj> income;
    DateTime from, to;
    final DateTimeFormatter df = DateTimeFormat.forPattern("dd-MM-yyyy");
    int type = 2;
    //endregion

    //region Constructor
    public IncomeFragment() {
        // Required empty public constructor
    }

    public static IncomeFragment newInstance() {
        IncomeFragment fragment = new IncomeFragment();
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_income, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            from = new DateTime();
            to = new DateTime();
            ivBackground = view.findViewById(R.id.iv_background);
            txtIncomeFromDate = view.findViewById(R.id.txt_income_from_date);
            txtIncomeToDate = view.findViewById(R.id.txt_income_to_date);

            DateTime toDay = new DateTime();
//            from = new DateTime(toDay.getYear(), toDay.getMonthOfYear(), toDay.getDayOfMonth()-7, 0, 0);
            from = new DateTime().minusWeeks(1);
            to = toDay;
            txtIncomeFromDate.setText(getDate(from));
            txtIncomeToDate.setText(getDate(to));

            llNumIncomeOrder = view.findViewById(R.id.ll_num_income_order);
            llPayed = view.findViewById(R.id.ll_payed);
            llNotPayed = view.findViewById(R.id.ll_not_payed);
            llCompared = view.findViewById(R.id.ll_compared);

            txtType = view.findViewById(R.id.txt_type);
            txtNumberOrder = view.findViewById(R.id.txt_number_order);
            ivAlert1 = view.findViewById(R.id.iv_alert_1);
            ivAlert2 = view.findViewById(R.id.iv_alert_2);
            ivBack = view.findViewById(R.id.iv_back);
            recyclerListIncome = view.findViewById(R.id.recycle_income);
            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerListIncome.setLayoutManager(layoutManager);
            type = 2;
            new GetIncomePending().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, from, to, 2);
            new GetNotify().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            getView().findViewById(R.id.ll_from_date).setOnClickListener(IncomeFragment.this);
            getView().findViewById(R.id.ll_to_date).setOnClickListener(IncomeFragment.this);
            getView().findViewById(R.id.iv_back).setOnClickListener(IncomeFragment.this);
            llPayed.setOnClickListener(IncomeFragment.this);
            llNotPayed.setOnClickListener(IncomeFragment.this);
            llCompared.setOnClickListener(this);
            isLoad = true;
        }
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
//                ivBack.setOnClickListener(null);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(2000);
//                            ivBack.setOnClickListener(IncomeFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                getActivity().getSupportFragmentManager().popBackStack();
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HomeFragment.newInstance());
//                CmmFunc.pop(getActivity());
                break;
            case R.id.ll_from_date:
                getIncomeFromDate();
                break;
            case R.id.ll_to_date:
                getIncomeToDate();
                break;
            case R.id.ll_not_payed:
                updateUI(view.getId());
                type = 2;
                llNumIncomeOrder.setVisibility(View.GONE);
                new GetIncomePending().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, from, to, 2);
                break;
            case R.id.ll_compared:
                updateUI(view.getId());
                type = 3;
                llNumIncomeOrder.setVisibility(View.GONE);
                new GetIncomePending().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, from, to, 3);
                break;
            case R.id.ll_payed:
                updateUI(view.getId());
                type = 1;
                llNumIncomeOrder.setVisibility(View.GONE);
                new GetIncomePending().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, from, to, 1);
                break;
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetIncomePending().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, from, to, type);
            }
        });

    }
    //endregion

    //region Methods

    //region GetIncomeFromDate
    private void getIncomeFromDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            String startDate = formatDateDDMMYYYY(year, monthOfYear + 1, dayOfMonth);
                            DateTime temp = DateTime.parse(startDate, df);

                            if (to.getMillis() < temp.getMillis()) {
                                Toast.makeText(getContext(), "Thời gian đến không được trước thời gian bắt đầu", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            from = temp;
                            txtIncomeFromDate.setText(getDate(from));
                            llNumIncomeOrder.setVisibility(View.GONE);
                            new GetIncomePending().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, from, to, type);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, from.getYear(), from.getMonthOfYear() - 1, from.getDayOfMonth());
        datePickerDialog.show();
    }
    //endregion

    //region GetIncomeToDate
    private void getIncomeToDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            DateTime temp = new DateTime(year, monthOfYear + 1, dayOfMonth, to.getHourOfDay(), to.getMinuteOfHour());
                            if (temp.getMillis() < from.getMillis()) {
                                //thong bao nho hon fromTIme
                                Toast.makeText(getContext(), "Thời gian đến không được trước thời gian bắt đầu", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            to = temp;
                            txtIncomeToDate.setText(getDate(to));
                            llNumIncomeOrder.setVisibility(View.GONE);
                            new GetIncomePending().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, from, to, type);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, to.getYear(), to.getMonthOfYear() - 1, to.getDayOfMonth());
        datePickerDialog.show();
    }
    //endregion

    //region UpdateUI
    private void updateUI(int id) {
        int fourdp = CmmFunc.dpToPixel(getActivity(), 4);
        switch (id) {
            case R.id.ll_not_payed:
                ivBackground.setPadding(0, 0, fourdp, 0);
                ivBackground.setImageResource(R.drawable.tab_left);
                txtType.setVisibility(View.GONE);
                break;
            case R.id.ll_payed:
                ivBackground.setPadding(fourdp, 0, 0, 0);
                ivBackground.setImageResource(R.drawable.tab_right);
                txtType.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_compared:
                ivBackground.setPadding(fourdp, 0, fourdp, 0);
                ivBackground.setImageResource(R.drawable.tab_mid);
                txtType.setVisibility(View.VISIBLE);
                break;
        }
    }
    //endregion

    //region FormatDate
    private String formatDateDDMMYYYY(int year, int month, int day) {
        String value = StringUtils.leftPad(day + "", 2, "0") + "-" + StringUtils.leftPad(month + "", 2, "0") + "-" + year;
        return value;
    }
    //endregion

    //region Format date API
    private String formatDateAPI(int year, int month, int day) {
        String value = year + "-" + StringUtils.leftPad(month + "", 2, "0") + "-" + StringUtils.leftPad(day + "", 2, "0");
        return value;
    }
    //endregion

    //region GetDate
    private String getDate(DateTime dateTime) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        return StringUtils.leftPad(dateTime.getDayOfMonth() + "", 2, "0") + " - " +
                StringUtils.leftPad(dateTime.getMonthOfYear() + "", 2, "0") + " - " + dateTime.getYear();
    }
    //endregion

    //endregion

    //region Request API

    //region Get IncomePening Debt
    class GetIncomePending extends Async {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected JSONObject doInBackground(Object... params) {
            int type;
            JSONObject jsonObject = null;
            try {
                DateTime fromTime = (DateTime) params[0];
                DateTime toTime = (DateTime) params[1];
                type = (int) params[2];
                String startTime = formatDateAPI(fromTime.getYear(), fromTime.getMonthOfYear(), fromTime.getDayOfMonth());
                String endTime = formatDateAPI(toTime.getYear(), toTime.getMonthOfYear(), toTime.getDayOfMonth());
                String value = APIHelper.getDebt(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), StorageHelper.get(StorageHelper.USERNAME),
                        StorageHelper.get(StorageHelper.TOKEN), startTime, endTime, type);
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
                    JSONObject data = jsonObject.getJSONObject("data");
                    double total = data.getDouble("total_income");
                    String strTotal = CmmFunc.formatMoney(Math.round(total));
                    ((TextView) getView().findViewById(R.id.txt_total_debt)).setText(strTotal + " VND");

                    income = (List<IncomeObj>) CmmFunc.tryParseList(data.getString("orders"),
                            IncomeObj.class);
                    IncomeAdapter incomeAdapter = new IncomeAdapter(IncomeFragment.this, recyclerListIncome, income, type);
                    recyclerListIncome.setAdapter(incomeAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
    //endregion

    //region GetNotify
    class GetNotify extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getNotify(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN));
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
                    JSONObject data = jsonObject.getJSONObject("data");
                    int notify_not_payment = data.getInt("notify_not_payment");
                    int notify_was_payment = data.getInt("notify_was_payment");
                    if (notify_not_payment > 0) {
                        ivAlert1.setVisibility(View.VISIBLE);
                    } else {
                        ivAlert1.setVisibility(View.GONE);
                    }
                    if (notify_was_payment > 0) {
                        ivAlert2.setVisibility(View.VISIBLE);
                    } else {
                        ivAlert2.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion
}

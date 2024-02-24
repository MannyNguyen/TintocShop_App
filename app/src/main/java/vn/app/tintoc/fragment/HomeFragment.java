package vn.app.tintoc.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import vn.app.tintoc.LoginActivity;
import vn.app.tintoc.R;
import vn.app.tintoc.adapter.LeftMenuAdapter;
import vn.app.tintoc.adapter.OrderResultAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.LeftMenu;
import vn.app.tintoc.model.OrderResultObj;
import vn.app.tintoc.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    //region Var
    ImageView ivMenu;
    private TextView txtFromDate, txtToDate;
    public LinearLayout llNumOrder;
    public TextView txtNumOrder;
    private EditText edtKeySearch;
    private String keySearch = "";
    private RecyclerView recyclerListOrder;
    private List<OrderResultObj> orderResult;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView ivAddnew;

    DateTime from, to;
    final DateTimeFormatter df = DateTimeFormat.forPattern("dd-MM-yyyy");
    //endregion

    //region Constructor
    public HomeFragment() {
        // Required empty public constructor
    }
    //endregion

    //region newInstance
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(400);
            swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
            recyclerListOrder = view.findViewById(R.id.recycler_list_order);
            llNumOrder = view.findViewById(R.id.ll_num_order);
            edtKeySearch = view.findViewById(R.id.edt_key_search);
            txtFromDate = view.findViewById(R.id.from_date);
            txtToDate = view.findViewById(R.id.to_date);
            ivMenu = view.findViewById(R.id.iv_menu);
            ivAddnew = view.findViewById(R.id.iv_addnew);
            txtNumOrder = view.findViewById(R.id.txt_num_order);

            DateTime toDay = new DateTime();
//            from = new DateTime(toDay.getYear(), toDay.getMonthOfYear(), toDay.getDayOfMonth()-7, 0, 0);
            from = new DateTime().minusWeeks(1);
            to = toDay;

            //Must set LinearLayoutManager for recyclerview
            ivMenu.setOnClickListener(HomeFragment.this);
            ivAddnew.setOnClickListener(HomeFragment.this);
            txtFromDate.setOnClickListener(HomeFragment.this);
            txtToDate.setOnClickListener(HomeFragment.this);

            edtKeySearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String key = edtKeySearch.getText().toString();
                    if (key.equals("")) {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtKeySearch.setTypeface(customFont);
                        return;
                    } else {
                        if (edtKeySearch.length() > 1) {
                            return;
                        }
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtKeySearch.setTypeface(customFont);
                    }
                }
            });

            //region Key search (khi bấm nút enter trên bàn phím)
            edtKeySearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        keySearch = edtKeySearch.getText().toString();
                        new getFilterOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keySearch, from, to);
                    }
                    return false;
                }
            });
            //endregion

//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerListOrder.setLayoutManager(layoutManager);
            txtFromDate.setText(getDate(from));
            txtToDate.setText(getDate(to));

            //Check Extra có rỗng không để lấy giá trị mở màn hình create order fragment
//                                if (getActivity().getIntent().getExtras()!=null){
//                                    int allowCreate = getActivity().getIntent().getExtras().getInt("ALLOW_CREATE");
//                                    if (allowCreate!=1){
//                                        CmmFunc.replaceFragment(getActivity(), R.id.main_container, CreateOrderFragment.newInstance(0, ""));
//                                    }
//                                }
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
            isLoad = true;
        }
        //Đặt ở đây để khi user mở lại màn hình thì reload lại data
        new getFilterOrder().executeOnExecutor(Async.THREAD_POOL_EXECUTOR, edtKeySearch.getText().toString(), from, to);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getFilterOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, edtKeySearch.getText().toString(), from, to);
            }
        });
    }
    //endregion

    //region Event
    //region OnClick
//    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_addnew:
//                CmmFunc.replaceFragment(getActivity(), R.id.main_container, new NewOrderFragment());
//                try {
//                    Thread.sleep(2000);
//                    CmmFunc.addFragment(getActivity(), R.id.main_container, CreateOrderFragment.newInstance(0, ""));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                CmmFunc.addFragment(getActivity(), R.id.main_container, CreateOrderFragment.newInstance(0, ""));
                break;
            case R.id.from_date:
                fromDateDialog();
                break;
            case R.id.to_date:
                toDateDialog();
                break;
        }
    }
    //endregion

    //region OnResume
    @Override
    public void onResume() {
        super.onResume();
        //Lock left menu after load
        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //When open keyboard won't affect to view and item on the screen
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new GetNotify().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//        new getFilterOrder().executeOnExecutor(Async.THREAD_POOL_EXECUTOR, edtKeySearch.getText().toString(), from, to);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new getFilterOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, edtKeySearch.getText().toString(), from, to);
//            }
//        });
    }
    //endregion

    //region OnPause
    @Override
    public void onPause() {
        super.onPause();
        DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    //endregion
    //endregion

    //region Methods

    //region FromDateDialog
    private void fromDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            String startDate = formatDateDDMMYYYY(year, monthOfYear + 1, dayOfMonth);
                            DateTime temp = DateTime.parse(startDate, df);
                            //So sanh với ToTime
                            if (to.getMillis() < temp.getMillis()) {
                                Toast.makeText(getContext(), getString(R.string.err_date_filter), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            from = temp;
                            txtFromDate.setText(getDate(from));

                            new getFilterOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", from, to);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, from.getYear(), from.getMonthOfYear() - 1, from.getDayOfMonth());
        datePickerDialog.show();
    }
    //endregion

    //region ToDateDialog
    private void toDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        DateTime temp = new DateTime(year, monthOfYear + 1, dayOfMonth, to.getHourOfDay(), to.getMinuteOfHour());
                        if (temp.getMillis() < from.getMillis()) {
                            //thong bao nho hon fromTIme
                            Toast.makeText(getContext(), getString(R.string.err_date_filter), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        to = temp;
                        txtToDate.setText(getDate(to));

                        new getFilterOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", from, to);
                    }
                }, to.getYear(), to.getMonthOfYear() - 1, to.getDayOfMonth());
        datePickerDialog.show();
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

    //region GetListOrder
    class GetListOrder extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getListOrder(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
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
                    orderResult = (List<OrderResultObj>) CmmFunc.tryParseList(jsonObject.getString("data"),
                            OrderResultObj.class);
                    OrderResultAdapter orderResultAdapter = new OrderResultAdapter(HomeFragment.this, recyclerListOrder, orderResult);
                    recyclerListOrder.setAdapter(orderResultAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region GetFilterOrder
    class getFilterOrder extends Async {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String key = (String) objects[0];
                DateTime fromTime = (DateTime) objects[1];
                DateTime toTime = (DateTime) objects[2];

                String startTime = formatDateAPI(fromTime.getYear(), fromTime.getMonthOfYear(), fromTime.getDayOfMonth());
                String endTime = formatDateAPI(toTime.getYear(), toTime.getMonthOfYear(), toTime.getDayOfMonth());
                //Thêm URLEncoder.encode(key,"UTF-8") để dùng cho ký tự có dấu và khoảng trắng.
                String value = APIHelper.filterOrder(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), URLEncoder.encode(key, "UTF-8"), startTime, endTime);
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
                    orderResult = (List<OrderResultObj>) CmmFunc.tryParseList(jsonObject.getString("data"),
                            OrderResultObj.class);
                    OrderResultAdapter orderResultAdapter = new OrderResultAdapter(HomeFragment.this, recyclerListOrder, orderResult);
                    recyclerListOrder.setAdapter(orderResultAdapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
    //endregion

    //region Get notify
    class GetNotify extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            String value = APIHelper.getNotify(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                    StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN));
            try {
                jsonObject = new JSONObject(value);
            } catch (JSONException e) {
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

                    RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_left_menu);
                    LeftMenuAdapter adapter = (LeftMenuAdapter) recyclerView.getAdapter();
                    LeftMenu incomeManager = LeftMenu.getByType(LeftMenu.INCOME, adapter.leftMenuList);

                    if (notify_not_payment != 0 || notify_was_payment != 0) {
                        incomeManager.setShowNotify(true);
                    } else {
                        incomeManager.setShowNotify(false);
                    }

                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion

}

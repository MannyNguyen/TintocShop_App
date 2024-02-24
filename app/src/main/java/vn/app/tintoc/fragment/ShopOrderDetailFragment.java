package vn.app.tintoc.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import vn.app.tintoc.R;
import vn.app.tintoc.adapter.OrderStatusAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.OrderStatusObj;
import vn.app.tintoc.utils.Utility;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static vn.app.tintoc.helper.StorageHelper.TOKEN;
import static vn.app.tintoc.helper.StorageHelper.USERNAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopOrderDetailFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    private TextView txtOrderCode, txtReason, txtSenderName,
            txtOrderFeeShip, txtSenderPhone, txtSenderAddress,
            txtReceiverName, txtReceiverPhone, txtReceiverAddress, txtMoneyCod, txtCashAdvance, txtSubFee, txtMediateFee,
            txtInsuranceFee, txtTotalGuestReceipts, txtMoneyMustShopReceive, txtUpdate, txtShopOrderCode;
    TextView txtDetailTransport, txtDetailShippingFee, txtDetailDate, txtDetailFromTime, txtDetailToTime, txtDetailCodMoney, txtDetailPayer, txtDetailNote, txtDetailCommodityType, txtDetailWeight,
            txtDetailSurcharge, txtDetailOrderCommodityFee, txtDetailInsuranceFee, txtTitleShopCode, txtNoteCenter;
    CheckBox chkDetailSeeCommodity, chkDetailChangeCommodity;
    TextView titleSubFee, txtOrderType, titleOrderFeeShip, titleMediateFee, titleInsuranceFee, subPayer, orderFeeShipPayer, mediateFeePayer,
            insuranceFeePayer, txtNotifyPayer, txtAlertCod;
    Button btnRating;
    RecyclerView recyclerStatus;
    private List<OrderStatusObj> orderStatusObjs;
    ImageView ivBack, ivCallSender, ivSMSSender, ivCallReceiver, ivSMSReceiver, ivIconUpdate;
    LinearLayout llOrderUpdate, btnCopy, llHistory;
    String strNewCreate;
    private int statusId;
    //endregion

    //region newInstance
    public static ShopOrderDetailFragment newInstance(String orderCode, String user_name, String token, boolean isGuest) {
        Bundle args = new Bundle();
        args.putString("orderCode", orderCode);
        args.putString("user_name", user_name);
        args.putString("token", token);
        args.putBoolean("isGuest", isGuest);
        ShopOrderDetailFragment fragment = new ShopOrderDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region Constructor
    public ShopOrderDetailFragment() {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_shop_order_detail, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivBack = view.findViewById(R.id.iv_back);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerStatus = view.findViewById(R.id.recycler_status);
            recyclerStatus.setLayoutManager(linearLayoutManager);
            txtDetailTransport = view.findViewById(R.id.txt_detail_transport);
            txtDetailShippingFee = view.findViewById(R.id.txt_detail_shipping_fee);
            txtDetailDate = view.findViewById(R.id.txt_detail_date);
            txtDetailFromTime = view.findViewById(R.id.txt_detail_from_time);
            txtDetailToTime = view.findViewById(R.id.txt_detail_to_time);
            txtDetailCodMoney = view.findViewById(R.id.txt_detail_cod_money);
            txtDetailPayer = view.findViewById(R.id.txt_detail_payer);
            chkDetailSeeCommodity = view.findViewById(R.id.chk_detail_see_commodity);
            chkDetailChangeCommodity = view.findViewById(R.id.chk_detail_change_commodity);
            txtDetailNote = view.findViewById(R.id.txt_detail_note);
            txtDetailCommodityType = view.findViewById(R.id.txt_detail_commodity_type);
            txtDetailWeight = view.findViewById(R.id.txt_detail_weight);
            txtDetailSurcharge = view.findViewById(R.id.txt_detail_surcharge);
            txtDetailOrderCommodityFee = view.findViewById(R.id.txt_detail_order_commodity_fee);
            txtDetailInsuranceFee = view.findViewById(R.id.txt_detail_insurance_fee);
            txtNoteCenter = view.findViewById(R.id.txt_note_center);
            txtOrderCode = view.findViewById(R.id.txt_order_code);
            txtShopOrderCode = view.findViewById(R.id.txt_shop_order_code);
            txtOrderType = view.findViewById(R.id.txt_order_type);
            txtReason = view.findViewById(R.id.txt_reason);
            txtSenderName = view.findViewById(R.id.txt_sender_name);
            txtSenderPhone = view.findViewById(R.id.txt_sender_phone);
            txtSenderAddress = view.findViewById(R.id.txt_sender_address);
            txtReceiverName = view.findViewById(R.id.txt_receiver_name);
            txtReceiverPhone = view.findViewById(R.id.txt_receiver_phone);
            txtReceiverAddress = view.findViewById(R.id.txt_receiver_address);
            txtMoneyCod = view.findViewById(R.id.txt_money_cod);
            txtCashAdvance = view.findViewById(R.id.txt_cash_advance);
            txtSubFee = view.findViewById(R.id.txt_sub_fee);
            txtOrderFeeShip = view.findViewById(R.id.txt_order_fee_ship);
            txtMediateFee = view.findViewById(R.id.txt_mediate_fee);
            txtInsuranceFee = view.findViewById(R.id.txt_insurance_fee);
            txtTotalGuestReceipts = view.findViewById(R.id.txt_total_guest_receipts);
            txtMoneyMustShopReceive = view.findViewById(R.id.txt_money_must_shop_receive);
            llOrderUpdate = view.findViewById(R.id.ll_order_update);
            ivIconUpdate = view.findViewById(R.id.iv_icon_update);
            txtUpdate = view.findViewById(R.id.txt_update);
            btnCopy = view.findViewById(R.id.btn_copy);
            titleSubFee = view.findViewById(R.id.title_sub_fee);
            titleOrderFeeShip = view.findViewById(R.id.title_order_fee_ship);
            titleMediateFee = view.findViewById(R.id.title_mediate_fee);
            titleInsuranceFee = view.findViewById(R.id.title_insurance_fee);
            subPayer = view.findViewById(R.id.sub_payer);
            orderFeeShipPayer = view.findViewById(R.id.order_fee_ship_payer);
            mediateFeePayer = view.findViewById(R.id.mediate_fee_payer);
            insuranceFeePayer = view.findViewById(R.id.insurance_fee_payer);
            txtNotifyPayer = view.findViewById(R.id.txt_notify_payer);
            ivCallSender = view.findViewById(R.id.iv_call_sender);
            ivSMSSender = view.findViewById(R.id.iv_sms_sender);
            ivCallReceiver = view.findViewById(R.id.iv_call_receiver);
            ivSMSReceiver = view.findViewById(R.id.iv_sms_receiver);
            txtAlertCod = view.findViewById(R.id.txt_alert_cod);
            btnRating = view.findViewById(R.id.btn_rating);
            llHistory = view.findViewById(R.id.ll_history);
            txtTitleShopCode = view.findViewById(R.id.txt_title_shop_code);

            if (getArguments().getBoolean("isGuest") == true) {
                llOrderUpdate.setVisibility(View.GONE);
                btnCopy.setVisibility(View.GONE);
                llHistory.setVisibility(View.GONE);
                new GetGuestOrderByCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new GetOrderByCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            ivBack.setOnClickListener(this);
            getView().findViewById(R.id.btn_call_support).setOnClickListener(this);
            getView().findViewById(R.id.btn_sms_support).setOnClickListener(this);
            llHistory.setOnClickListener(this);
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
//                            ivBack.setOnClickListener(ShopOrderDetailFragment.this);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                getActivity().getSupportFragmentManager().popBackStack();
                CmmFunc.pop(getActivity());
//                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HomeFragment.newInstance());
                break;
            case R.id.btn_call_support:
                Intent iHotline_1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0938264867", null));
                startActivity(iHotline_1);
                break;
            case R.id.btn_sms_support:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "0938264867", null)));
                break;
            case R.id.ll_history:
                CmmFunc.addFragment(getActivity(), R.id.main_container, HistoryDialogFragment.newInstance(getArguments().getString("orderCode")));
                break;
        }
    }
    //endregion

    //region Request API

    //region Get order by code
    class GetOrderByCode extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getOrderByCode(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(USERNAME), StorageHelper.get(TOKEN), getArguments().getString("orderCode"));
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

                    //Parse object thành String, gửi qua màn hình tạo đơn hàng, chức năng copy/edit
                    strNewCreate = jsonObject.getString("data");
                    //=data.toString();

                    boolean canRate = data.getBoolean("rated");
                    if (canRate) {
                        btnRating.setVisibility(View.GONE);
                    } else {
                        btnRating.setVisibility(View.VISIBLE);
                    }

                    //region Check xem đơn hàng này có được phép edit/update không
                    boolean canEdit = data.getBoolean("can_edit");
                    if (canEdit) {
                        llOrderUpdate.setEnabled(true);
                    } else {
                        llOrderUpdate.setEnabled(false);
                        ivIconUpdate.setImageResource(R.drawable.ic_disable_edit);
                        txtUpdate.setTextColor(getResources().getColor(R.color.gray_transparent_main));
                    }

                    //Update đơn hàng, mở màn hình create order
                    llOrderUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CmmFunc.addFragment(getActivity(), R.id.main_container, CreateOrderFragment.newInstance(1, strNewCreate));
                        }
                    });

                    //Copy đơn hàng, mở màn hình create order
                    btnCopy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CmmFunc.addFragment(getActivity(), R.id.main_container, CreateOrderFragment.newInstance(2, strNewCreate));
                        }
                    });
                    //endregion

                    //region Thanh status bar
                    statusId = data.getInt("status_id");
                    orderStatusObjs = (List<OrderStatusObj>) CmmFunc.tryParseList(data.getString("status_process"),
                            OrderStatusObj.class);
                    OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(ShopOrderDetailFragment.this, recyclerStatus,
                            orderStatusObjs, statusId);
                    recyclerStatus.setAdapter(orderStatusAdapter);
                    //endregion

                    //region Ẩn hiện lý do
                    if (data.getString("reason_confirm").equals("") || data.getString("reason_confirm").equals(null)) {
                        txtReason.setVisibility(View.GONE);
                    } else {
                        txtReason.setVisibility(View.VISIBLE);
                        txtReason.setText(data.getString("reason_confirm"));
                    }
                    //endregion

                    final String orderCode = data.getString("order_code");
                    txtOrderCode.setText(data.getString("order_code"));

                    if (data.getString("shop_order_code").equals("")) {
                        txtTitleShopCode.setVisibility(View.GONE);
                        txtShopOrderCode.setVisibility(View.GONE);
                    } else {
                        txtShopOrderCode.setText(data.getString("shop_order_code"));
                    }

                    txtOrderType.setText("(" + data.getString("addon_text") + ")");

                    if (data.getString("note_center").equals("")) {
                        txtNoteCenter.setVisibility(View.GONE);
                    } else {
                        txtNoteCenter.setVisibility(View.GONE);
                        txtNoteCenter.setText("(" + data.getString("note_center") + ")");
                    }

                    //region Định dạng ngày tháng - thời gian
                    DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    String createDate = data.getString("create_date");
                    if (createDate != null && createDate != "") {
                        DateTime dateTime = DateTime.parse(createDate, df);
                        String value = formatDay(dateTime);
                        txtDetailDate.setText(value);
                    }

                    String timeDeliveryFrom = data.getString("time_delivery_from");
                    if (timeDeliveryFrom != null && timeDeliveryFrom != "") {
                        DateTime dateTime = DateTime.parse(timeDeliveryFrom, df);
                        String value = formatTime(dateTime);
                        txtDetailFromTime.setText(value);
                    }

                    String timeDeliveryTo = data.getString("time_delivery_to");
                    if (timeDeliveryTo != null && timeDeliveryTo != "") {
                        DateTime dateTime = DateTime.parse(timeDeliveryTo, df);
                        String value = formatTime(dateTime);
                        txtDetailToTime.setText(value);
                    }
                    //endregion

                    txtSenderName.setText(data.getString("sender_fullname"));
                    txtSenderPhone.setText(data.getString("sender_phone"));
                    txtSenderAddress.setText(data.getString("sender_full_address"));
                    txtReceiverName.setText(data.getString("receiver_fullname"));
                    txtReceiverPhone.setText(data.getString("receiver_phone"));
                    txtReceiverAddress.setText(data.getString("receiver_full_address"));

                    //copy here
                    txtDetailTransport.setText(data.getString("form_delivery_name"));
                    JSONObject shippingFee = data.getJSONObject("shipping");
                    txtDetailShippingFee.setText(CmmFunc.formatMoney(Math.round(shippingFee.getDouble("money"))) + " VND");
                    if (shippingFee.getInt("payer") == 1) {
                        txtDetailPayer.setText("Người gửi");
                    }
                    if (shippingFee.getInt("payer") == 2) {
                        txtDetailPayer.setText("Người nhận");
                    }
                    txtDetailCodMoney.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_cod"))) + "");

                    if (data.getInt("allow_see_order") == 0) {
                        chkDetailSeeCommodity.setChecked(false);
                    }
                    if (data.getInt("allow_see_order") == 1) {
                        chkDetailSeeCommodity.setChecked(true);
                    }

                    if (data.getBoolean("is_return_product")) {
                        chkDetailChangeCommodity.setChecked(true);
                    } else {
                        chkDetailChangeCommodity.setChecked(false);
                    }

                    txtDetailNote.setText(data.getString("note"));
                    txtDetailCommodityType.setText(data.getString("product_type"));
                    txtDetailWeight.setText(data.getDouble("product_weight_kg") + "");

                    JSONObject subObj = data.getJSONObject("sub");
                    txtDetailSurcharge.setText(CmmFunc.formatMoney(Math.round(subObj.getDouble("money"))) + "");

                    txtDetailOrderCommodityFee.setText(CmmFunc.formatMoney(Math.round(data.getDouble("product_value"))) + "");

                    JSONObject protectObj = data.getJSONObject("protect");
                    txtDetailInsuranceFee.setText(CmmFunc.formatMoney(Math.round(protectObj.getDouble("money"))) + "");

                    //end here

                    txtMoneyCod.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_cod"))) + " VND");
                    txtCashAdvance.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_cash_advance"))) + " VND");
                    txtTotalGuestReceipts.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_must_take_user"))) + " VND");

                    double moneyShop = data.getDouble("money_must_shop_receive");
                    txtMoneyMustShopReceive.setText(CmmFunc.formatMoney(Math.round(moneyShop)) + " VND");

                    //region Ẩn các phí = 0, hiển thị thông báo người trả phí là người nhận
                    JSONObject sub = data.getJSONObject("sub");
                    if (sub.getDouble("money") == 0) {
                        subPayer.setVisibility(View.GONE);
                        titleSubFee.setVisibility(View.GONE);
                        txtSubFee.setVisibility(View.GONE);
                    } else {
                        if (sub.getInt("payer") == 1) {
                            subPayer.setVisibility(View.GONE);
                            titleSubFee.setVisibility(View.VISIBLE);
                            txtSubFee.setVisibility(View.VISIBLE);
                        }
                        if (sub.getInt("payer") == 2) {
                            subPayer.setVisibility(View.VISIBLE);
                            titleSubFee.setVisibility(View.VISIBLE);
                            txtSubFee.setVisibility(View.VISIBLE);
                        }
                        txtSubFee.setText(CmmFunc.formatMoney(Math.round(sub.getDouble("money"))) + " VND");
                    }

                    JSONObject shipping = data.getJSONObject("shipping");
                    if (shipping.getDouble("money") == 0) {
                        orderFeeShipPayer.setVisibility(View.GONE);
                        titleOrderFeeShip.setVisibility(View.GONE);
                        txtOrderFeeShip.setVisibility(View.GONE);
                    } else {
                        if (shipping.getInt("payer") == 1) {
                            orderFeeShipPayer.setVisibility(View.GONE);
                            titleOrderFeeShip.setVisibility(View.VISIBLE);
                            txtOrderFeeShip.setVisibility(View.VISIBLE);
                        }
                        if (shipping.getInt("payer") == 2) {
                            orderFeeShipPayer.setVisibility(View.VISIBLE);
                            titleOrderFeeShip.setVisibility(View.VISIBLE);
                            txtOrderFeeShip.setVisibility(View.VISIBLE);
                        }
                        txtOrderFeeShip.setText(CmmFunc.formatMoney(Math.round(shipping.getDouble("money"))) + " VND");
                    }

                    JSONObject medial = data.getJSONObject("medial");
                    if (medial.getDouble("money") == 0) {
                        titleMediateFee.setVisibility(View.GONE);
                        txtMediateFee.setVisibility(View.GONE);
                        mediateFeePayer.setVisibility(View.GONE);
                        txtAlertCod.setVisibility(View.GONE);
                    } else {
                        if (medial.getInt("payer") == 1) {
                            mediateFeePayer.setVisibility(View.GONE);
                            titleMediateFee.setVisibility(View.VISIBLE);
                            txtMediateFee.setVisibility(View.VISIBLE);
                            txtAlertCod.setVisibility(View.VISIBLE);
                        }
                        if (medial.getInt("payer") == 2) {
                            mediateFeePayer.setVisibility(View.VISIBLE);
                            titleMediateFee.setVisibility(View.VISIBLE);
                            txtMediateFee.setVisibility(View.VISIBLE);
                            txtAlertCod.setVisibility(View.VISIBLE);
                        }
                        txtMediateFee.setText(CmmFunc.formatMoney(Math.round(medial.getDouble("money"))) + " VND");
                    }

                    JSONObject protect = data.getJSONObject("protect");
                    if (protect.getDouble("money") == 0) {
                        titleInsuranceFee.setVisibility(View.GONE);
                        txtInsuranceFee.setVisibility(View.GONE);
                        insuranceFeePayer.setVisibility(View.GONE);
                    } else {
                        if (protect.getInt("payer") == 1) {
                            insuranceFeePayer.setVisibility(View.GONE);
                            titleInsuranceFee.setVisibility(View.VISIBLE);
                            txtInsuranceFee.setVisibility(View.VISIBLE);
                        }
                        if (protect.getInt("payer") == 2) {
                            insuranceFeePayer.setVisibility(View.VISIBLE);
                            titleInsuranceFee.setVisibility(View.VISIBLE);
                            txtInsuranceFee.setVisibility(View.VISIBLE);
                        }
                        txtInsuranceFee.setText(CmmFunc.formatMoney(Math.round(protect.getDouble("money"))) + " VND");
                    }

                    if (sub.getInt("payer") == 2 || shipping.getInt("payer") == 2 || medial.getInt("payer") == 2 || protect.getInt("payer") == 2) {
                        txtNotifyPayer.setVisibility(View.VISIBLE);
                    } else {
                        txtNotifyPayer.setVisibility(View.GONE);
                    }
                    //endregion

                    //region Xử lý link hình ảnh order
                    String str = data.getString("images");
                    JSONArray images = new JSONArray(str);
                    for (int i = 0; i < images.length(); i++) {
                        final String URL = images.getString(i);
                        if (i == 0) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_1));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_1).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }
                        } else if (i == 1) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_2));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_2).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }
                        } else if (i == 2) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_3));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_3).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }
                        }
                    }
                    //endregion

                    final String senderPhone = data.getString("sender_phone");
                    final String receiverPhone = data.getString("receiver_phone");

                    //region Call and SMS
                    ivCallSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", senderPhone, null));
                            startActivity(callSender);
                        }
                    });

                    ivSMSSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", senderPhone, null)));
                        }
                    });

                    ivCallReceiver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", receiverPhone, null));
                            startActivity(callSender);
                        }
                    });

                    ivSMSReceiver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", receiverPhone, null)));
                        }
                    });
                    //endregion

                    //region Nút Rating
                    btnRating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CmmFunc.addFragment(getActivity(), R.id.main_container,
                                    DetailRatingDialogFragment.newInstance(orderCode));
                        }
                    });
                    //endregion
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Get guest order by code
    class GetGuestOrderByCode extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getGuestOrderByCode(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        getArguments().getString("user_name"), getArguments().getString("token"), getArguments().getString("orderCode"));
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

                    //Parse object thành String, gửi qua màn hình tạo đơn hàng
                    strNewCreate = jsonObject.getString("data");

                    //region Thanh status bar
                    statusId = data.getInt("status_id");
                    orderStatusObjs = (List<OrderStatusObj>) CmmFunc.tryParseList(data.getString("status_process"),
                            OrderStatusObj.class);
                    OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(ShopOrderDetailFragment.this, recyclerStatus,
                            orderStatusObjs, statusId);
                    recyclerStatus.setAdapter(orderStatusAdapter);
                    //endregion

                    boolean canRate = data.getBoolean("rated");
                    if (canRate) {
                        btnRating.setVisibility(View.GONE);
                    } else {
                        btnRating.setVisibility(View.VISIBLE);
                    }

                    final String orderCode = data.getString("order_code");
                    txtOrderCode.setText(data.getString("order_code"));
                    txtShopOrderCode.setText(data.getString("shop_order_code"));
                    txtOrderType.setText("(" + data.getString("addon_text") + ")");

                    if (data.getString("note_center").equals("")) {
                        txtNoteCenter.setVisibility(View.GONE);
                    } else {
                        txtNoteCenter.setVisibility(View.GONE);
                        txtNoteCenter.setText("(" + data.getString("note_center") + ")");
                    }

                    //region Format lại định dạng ngày tháng thời gian
                    DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    String createDate = data.getString("create_date");
                    if (createDate != null && createDate != "") {
                        DateTime dateTime = DateTime.parse(createDate, df);
                        String value = formatDay(dateTime);
                        txtDetailDate.setText(value);
                    }

                    String timeDeliveryFrom = data.getString("time_delivery_from");
                    if (timeDeliveryFrom != null && timeDeliveryFrom != "") {
                        DateTime dateTime = DateTime.parse(timeDeliveryFrom, df);
                        String value = formatTime(dateTime);
                        txtDetailFromTime.setText(value);
                    }

                    String timeDeliveryTo = data.getString("time_delivery_to");
                    if (timeDeliveryTo != null && timeDeliveryTo != "") {
                        DateTime dateTime = DateTime.parse(timeDeliveryTo, df);
                        String value = formatTime(dateTime);
                        txtDetailToTime.setText(value);
                    }
                    //endregion

                    txtSenderPhone.setText(data.getString("sender_phone"));
                    txtSenderAddress.setText(data.getString("sender_full_address"));
                    txtReceiverName.setText(data.getString("receiver_fullname"));
                    txtReceiverPhone.setText(data.getString("receiver_phone"));
                    txtReceiverAddress.setText(data.getString("receiver_full_address"));
                    txtMoneyCod.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_cod")) + " VND"));
                    txtCashAdvance.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_cash_advance"))) + " VND");
                    txtTotalGuestReceipts.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_must_take_user"))) + " VND");
                    double moneyShop = data.getDouble("money_must_shop_receive");
                    txtMoneyMustShopReceive.setText(CmmFunc.formatMoney(Math.round(moneyShop)) + " VND");

                    //region New
                    txtDetailTransport.setText(data.getString("form_delivery_name"));
                    JSONObject shippingFee = data.getJSONObject("shipping");
                    txtDetailShippingFee.setText(CmmFunc.formatMoney(Math.round(shippingFee.getDouble("money"))) + " VND");
                    if (shippingFee.getInt("payer") == 1) {
                        txtDetailPayer.setText("Người gửi");
                    }
                    if (shippingFee.getInt("payer") == 2) {
                        txtDetailPayer.setText("Người nhận");
                    }
                    txtDetailCodMoney.setText(CmmFunc.formatMoney(Math.round(data.getDouble("money_cod"))) + "");

                    if (data.getInt("allow_see_order") == 0) {
                        chkDetailSeeCommodity.setChecked(false);
                    }
                    if (data.getInt("allow_see_order") == 1) {
                        chkDetailSeeCommodity.setChecked(true);
                    }

                    if (data.getBoolean("is_return_product")) {
                        chkDetailChangeCommodity.setChecked(true);
                    } else {
                        chkDetailChangeCommodity.setChecked(false);
                    }

                    txtDetailNote.setText(data.getString("note"));
                    txtDetailCommodityType.setText("product_type");
                    txtDetailWeight.setText(data.getDouble("product_weight_kg") + "");

                    JSONObject subObj = data.getJSONObject("sub");
                    txtDetailSurcharge.setText(CmmFunc.formatMoney(Math.round(subObj.getDouble("money"))) + "");

                    txtDetailOrderCommodityFee.setText(CmmFunc.formatMoney(Math.round(data.getDouble("product_value"))) + "");

                    JSONObject protectObj = data.getJSONObject("protect");
                    txtDetailInsuranceFee.setText(CmmFunc.formatMoney(Math.round(protectObj.getDouble("money"))) + "");

                    //endregion

                    //region Ẩn các phí = 0, hiển thị thông báo người trả phí là người nhận
                    JSONObject sub = data.getJSONObject("sub");
                    if (sub.getDouble("money") == 0) {
                        subPayer.setVisibility(View.GONE);
                        titleSubFee.setVisibility(View.GONE);
                        txtSubFee.setVisibility(View.GONE);
                    } else {
                        if (sub.getInt("payer") == 1) {
                            subPayer.setVisibility(View.GONE);
                            titleSubFee.setVisibility(View.VISIBLE);
                            txtSubFee.setVisibility(View.VISIBLE);
                        }
                        if (sub.getInt("payer") == 2) {
                            subPayer.setVisibility(View.VISIBLE);
                            titleSubFee.setVisibility(View.VISIBLE);
                            txtSubFee.setVisibility(View.VISIBLE);
                        }
                        txtSubFee.setText(CmmFunc.formatMoney(Math.round(sub.getDouble("money"))) + " VND");
                    }

                    JSONObject shipping = data.getJSONObject("shipping");
                    if (shipping.getDouble("money") == 0) {
                        orderFeeShipPayer.setVisibility(View.GONE);
                        titleOrderFeeShip.setVisibility(View.GONE);
                        txtOrderFeeShip.setVisibility(View.GONE);
                    } else {
                        if (shipping.getInt("payer") == 1) {
                            orderFeeShipPayer.setVisibility(View.GONE);
                            titleOrderFeeShip.setVisibility(View.VISIBLE);
                            txtOrderFeeShip.setVisibility(View.VISIBLE);
                        }
                        if (shipping.getInt("payer") == 2) {
                            orderFeeShipPayer.setVisibility(View.VISIBLE);
                            titleOrderFeeShip.setVisibility(View.VISIBLE);
                            txtOrderFeeShip.setVisibility(View.VISIBLE);
                        }
                        txtOrderFeeShip.setText(CmmFunc.formatMoney(Math.round(shipping.getDouble("money"))) + " VND");
                    }

                    JSONObject medial = data.getJSONObject("medial");
                    if (medial.getDouble("money") == 0) {
                        titleMediateFee.setVisibility(View.GONE);
                        txtMediateFee.setVisibility(View.GONE);
                        mediateFeePayer.setVisibility(View.GONE);
                        txtAlertCod.setVisibility(View.GONE);
                    } else {
                        if (medial.getInt("payer") == 1) {
                            mediateFeePayer.setVisibility(View.GONE);
                            titleMediateFee.setVisibility(View.VISIBLE);
                            txtMediateFee.setVisibility(View.VISIBLE);
                            txtAlertCod.setVisibility(View.VISIBLE);
                        }
                        if (medial.getInt("payer") == 2) {
                            mediateFeePayer.setVisibility(View.VISIBLE);
                            titleMediateFee.setVisibility(View.VISIBLE);
                            txtMediateFee.setVisibility(View.VISIBLE);
                            txtAlertCod.setVisibility(View.VISIBLE);
                        }
                        txtMediateFee.setText(CmmFunc.formatMoney(Math.round(medial.getDouble("money"))) + " VND");
                    }

                    JSONObject protect = data.getJSONObject("protect");
                    if (protect.getDouble("money") == 0) {
                        titleInsuranceFee.setVisibility(View.GONE);
                        txtInsuranceFee.setVisibility(View.GONE);
                        insuranceFeePayer.setVisibility(View.GONE);
                    } else {
                        if (protect.getInt("payer") == 1) {
                            insuranceFeePayer.setVisibility(View.GONE);
                            titleInsuranceFee.setVisibility(View.VISIBLE);
                            txtInsuranceFee.setVisibility(View.VISIBLE);
                        }
                        if (protect.getInt("payer") == 2) {
                            insuranceFeePayer.setVisibility(View.VISIBLE);
                            titleInsuranceFee.setVisibility(View.VISIBLE);
                            txtInsuranceFee.setVisibility(View.VISIBLE);
                        }
                        txtInsuranceFee.setText(CmmFunc.formatMoney(Math.round(protect.getDouble("money"))) + " VND");
                    }

                    if (sub.getInt("payer") == 2 || shipping.getInt("payer") == 2 || medial.getInt("payer") == 2 || protect.getInt("payer") == 2) {
                        txtNotifyPayer.setVisibility(View.VISIBLE);
                    } else {
                        txtNotifyPayer.setVisibility(View.GONE);
                    }
                    //endregion

                    //region Xử lý link hình ảnh
                    String str = data.getString("images");
                    JSONArray images = new JSONArray(str);
                    for (int i = 0; i < images.length(); i++) {
                        final String URL = images.getString(i);
                        if (i == 0) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_1));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_1).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }
                        } else if (i == 1) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_2));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_2).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }
                        } else if (i == 2) {
                            Glide.with(getActivity()).load(URL).into((ImageView) view.findViewById(R.id.iv_photo_3));
                            if (URL != null && URL != "") {
                                getView().findViewById(R.id.iv_photo_3).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(URL));
                                    }
                                });
                            }
                        }
                    }
                    //endregion

                    //region Call and SMS
                    final String senderPhone = data.getString("sender_phone");
                    final String receiverPhone = data.getString("receiver_phone");

                    ivCallSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", senderPhone, null));
                            startActivity(callSender);
                        }
                    });

                    ivSMSSender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", senderPhone, null)));
                        }
                    });

                    ivCallReceiver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callSender = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", receiverPhone, null));
                            startActivity(callSender);
                        }
                    });

                    ivSMSReceiver.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", receiverPhone, null)));
                        }
                    });

                    btnRating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CmmFunc.addFragment(getActivity(), R.id.main_container,
                                    DetailRatingDialogFragment.newInstance(orderCode));
                        }
                    });
                    //endregion

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion

    //region Methods
    private String formatDate(DateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        String retValue = StringUtils.leftPad(dateTime.getHourOfDay() + "", 2, "0") + ":" + StringUtils.leftPad(dateTime.getMinuteOfHour() + "", 2, "0") + " Ngày " + StringUtils.leftPad(dateTime.getDayOfMonth() + "", 2, "0") + "-" + StringUtils.leftPad(dateTime.getMonthOfYear() + "", 2, "0") + "-" + dateTime.getYear();
        return retValue;
    }

    private String formatTime(DateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        String retValue = StringUtils.leftPad(dateTime.getHourOfDay() + "", 2, "0") + ":" + StringUtils.leftPad(dateTime.getMinuteOfHour() + "", 2, "0");
        return retValue;
    }

    private String formatDay(DateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        String retValue = StringUtils.leftPad(dateTime.getDayOfMonth() + "", 2, "0") + "-" + StringUtils.leftPad(dateTime.getMonthOfYear() + "", 2, "0") + "-" + dateTime.getYear();
        return retValue;
    }
    //endregion
}

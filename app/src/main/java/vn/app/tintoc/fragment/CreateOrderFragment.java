package vn.app.tintoc.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import vn.app.tintoc.R;
import vn.app.tintoc.adapter.AddressCreateOrderAdapter;
import vn.app.tintoc.adapter.FormDeliveryAdapter;
import vn.app.tintoc.adapter.WeightProductAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.config.GlobalClass;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.FormDeliveryObj;
import vn.app.tintoc.model.FormDeliveryServiceObj;
import vn.app.tintoc.model.RegionObj;
import vn.app.tintoc.model.ShopAddressObj;
import vn.app.tintoc.model.WeightProductObj;
import vn.app.tintoc.utils.Utility;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOrderFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //region Variables
    private static final int RESULT_PICK_CONTACT = 855;
    final DateTimeFormatter df = DateTimeFormat.forPattern("HH:mm:ss dd-MM-yyyy");
    final String format = "HH:mm:ss dd-MM-yyyy";
    final int CAPTURE = 1999;
    final int GALLERY = 2000;
    Thread threadInit;
    FrameLayout layoutCreate;
    ScrollView scrollView;
    String orderCode = "";
    public EditText senderPhone, senderAddress, shippingFee, codMoney, commodityFee, commodityName, edtShopOrderCode;
    EditText receiverFullName, receiverPhone, receiverAddress, note, edtWeight;
    TextView insuranceFee, txtReceiverRegion, txtAlertWeight, titleCreateOrder, surcharge;
    public TextView txtSenderRegion;
    ImageView upload1, upload2, upload3, removeUpload1, removeUpload2, removeUpload3, ivBack, ivShowHide, ivContacts;
    Spinner weight, serviceTransport, payTransport, spnNormalFast, spnAddress;
    Button confirm;
    LinearLayout llNormalFast, addOnContainer, llTransportService, llViettelPost, llTransport, llnameWeight, llPhotoNote;
    CheckBox chkSeeCommodity, chkChangeCommodity;
    RadioButton rdoVehicle, rdoViettel;
    int addOn, seeCommodity;
    boolean isShow = true;
    public int idSenderCity = 0;
    public int idSenderDistrict = 0;
    public int idSenderWard = 0;
    int idCity = 0;
    int idDistrict = 0;
    int idWard = 0;
    int typeAction;
    TextView fromTime, fromDate, toTime, toDate, txtUserPay;
    DateTime from, to;
    //Lưu giá trị mặc định khi load Profile/Config nếu -1 thì bỏ qua
    int defaultSenderDistrictID = -1;
    int defaultReceiverDistrictID = -1;
    int userRegion;
    double protectFeePercent;
    int idFormDelivery;
    List<FormDeliveryObj> formDelivery;
    //ID của bảo hiểm - lấy trong order config
    int protectID;
    List<FormDeliveryServiceObj> formServices;
    List<File> files = new ArrayList<>();
    List<WeightProductObj> weightProducts;
    List<WeightProductObj> weightProductPosts;
    List<ShopAddressObj> shopAddressObjs;
    JSONObject jsonObjectUpdate;
    public int idAddress;
    //Capture
    Uri imageUri;
    //endregion

    //region Contructors
    public CreateOrderFragment() {
    }
    //endregion

    //region Instance
    //Nếu type = 0 thì đơn hàng mới trống, type = 1, update đơn hàng, type = 2, copy đơn hàng
    public static CreateOrderFragment newInstance(int type, String dataValue) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putString("dataValue", dataValue);
        CreateOrderFragment fragment = new CreateOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region Init
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_create_order, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().getInt("type") != 0) {
            try {
                jsonObjectUpdate = new JSONObject(getArguments().getString("dataValue"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //clear list tỉnh thành trước vì là static
        RegionDialogFragment.regionObjList.clear();
        RegionDialogFragment.regionObjSenderList.clear();

        llTransport = view.findViewById(R.id.ll_transport);
        llnameWeight = view.findViewById(R.id.ll_name_weight);
        llPhotoNote = view.findViewById(R.id.ll_photo_note);
        txtSenderRegion = view.findViewById(R.id.txt_sender_region);
        txtReceiverRegion = view.findViewById(R.id.txt_receiver_region);
        if (!isLoad) {
            threadInit = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        from = new DateTime();
                        to = new DateTime();
                        ivBack = view.findViewById(R.id.back);
                        layoutCreate = view.findViewById(R.id.layout_create);
                        scrollView = view.findViewById(R.id.scroll_view);
                        titleCreateOrder = view.findViewById(R.id.title_create_order);
                        ivShowHide = view.findViewById(R.id.iv_show_hide);
                        spnAddress = view.findViewById(R.id.spn_address);
                        senderPhone = (EditText) view.findViewById(R.id.sender_phone);
                        senderAddress = (EditText) view.findViewById(R.id.sender_address);
                        receiverFullName = (EditText) view.findViewById(R.id.receiver_fullname);
                        receiverPhone = (EditText) view.findViewById(R.id.receiver_phone);
                        receiverAddress = (EditText) view.findViewById(R.id.receiver_address);
                        txtUserPay = view.findViewById(R.id.txt_user_pay);
                        commodityFee = (EditText) view.findViewById(R.id.commodity_fee);
                        commodityName = (EditText) view.findViewById(R.id.commodity_type);
                        codMoney = (EditText) view.findViewById(R.id.cod_money);
                        insuranceFee = (TextView) view.findViewById(R.id.insurance_fee);
                        llNormalFast = view.findViewById(R.id.ll_normal_fast);
                        llTransportService = view.findViewById(R.id.ll_transport_service);
                        llViettelPost = view.findViewById(R.id.ll_viettel_post);
                        rdoVehicle = view.findViewById(R.id.rdo_vehicle);
                        rdoViettel = view.findViewById(R.id.rdo_viettel);
                        weight = (Spinner) view.findViewById(R.id.weight);
                        edtWeight = view.findViewById(R.id.edt_weight);
                        txtAlertWeight = view.findViewById(R.id.txt_alert_weight);
                        fromTime = (TextView) view.findViewById(R.id.from_time);
                        toTime = (TextView) view.findViewById(R.id.to_time);
                        fromDate = (TextView) view.findViewById(R.id.from_date);
                        toDate = (TextView) view.findViewById(R.id.to_date);
                        serviceTransport = (Spinner) view.findViewById(R.id.service_transport);
                        shippingFee = (EditText) view.findViewById(R.id.shipping_fee);
                        payTransport = (Spinner) view.findViewById(R.id.pay_transport);
                        spnNormalFast = view.findViewById(R.id.spn_normal_fast);
                        note = (EditText) view.findViewById(R.id.note);
                        upload1 = (ImageView) view.findViewById(R.id.upload_1);
                        upload2 = (ImageView) view.findViewById(R.id.upload_2);
                        upload3 = (ImageView) view.findViewById(R.id.upload_3);
                        removeUpload1 = (ImageView) view.findViewById(R.id.remove_upload_1);
                        removeUpload2 = (ImageView) view.findViewById(R.id.remove_upload_2);
                        removeUpload3 = (ImageView) view.findViewById(R.id.remove_upload_3);
                        chkSeeCommodity = view.findViewById(R.id.chk_see_commodity);
                        chkChangeCommodity = view.findViewById(R.id.chk_change_commodity);
                        addOnContainer = view.findViewById(R.id.add_on_container);
                        confirm = (Button) view.findViewById(R.id.confirm);
                        surcharge = view.findViewById(R.id.surcharge);
                        edtShopOrderCode = view.findViewById(R.id.edt_shop_order_code);
                        ivContacts=view.findViewById(R.id.iv_contacts);

                        serviceTransport.setOnItemSelectedListener(CreateOrderFragment.this);
                        weight.setOnItemSelectedListener(CreateOrderFragment.this);
                        txtReceiverRegion.setOnClickListener(CreateOrderFragment.this);
                        fromTime.setOnClickListener(CreateOrderFragment.this);
                        toTime.setOnClickListener(CreateOrderFragment.this);
                        fromDate.setOnClickListener(CreateOrderFragment.this);
                        toDate.setOnClickListener(CreateOrderFragment.this);
                        upload1.setOnClickListener(CreateOrderFragment.this);
                        upload2.setOnClickListener(CreateOrderFragment.this);
                        upload3.setOnClickListener(CreateOrderFragment.this);
                        removeUpload1.setOnClickListener(CreateOrderFragment.this);
                        removeUpload2.setOnClickListener(CreateOrderFragment.this);
                        removeUpload3.setOnClickListener(CreateOrderFragment.this);
                        confirm.setOnClickListener(CreateOrderFragment.this);
                        spnAddress.setOnItemSelectedListener(CreateOrderFragment.this);
                        ivContacts.setOnClickListener(CreateOrderFragment.this);
                        //set text nút "tạo đơn hàng"
                        if (getArguments().getInt("type") == 1) {
                            confirm.setText(getString(R.string.edit));
                        }
                        if (getArguments().getInt("type") == 2) {
                            confirm.setText(getString(R.string.create));
                        }

                        ivBack.setOnClickListener(CreateOrderFragment.this);
                        rdoVehicle.setOnCheckedChangeListener(CreateOrderFragment.this);
                        rdoViettel.setOnCheckedChangeListener(CreateOrderFragment.this);
                        ivShowHide.setBackgroundResource(R.drawable.arrow_down);

                        //region setting font text
                        senderPhone.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String sdPhone = senderPhone.getText().toString();
                                if (sdPhone.equals("")) {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                                    senderPhone.setTypeface(customFont);
                                    return;
                                } else {
                                    if (senderPhone.length() > 1) {
                                        return;
                                    }
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                                    senderPhone.setTypeface(customFont);
                                }
                            }
                        });

                        senderAddress.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String sdAddress = senderAddress.getText().toString();
                                if (sdAddress.equals("")) {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                                    senderAddress.setTypeface(customFont);
                                    return;
                                } else {
                                    if (senderAddress.length() > 1) {
                                        return;
                                    }
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                                    senderAddress.setTypeface(customFont);
                                }
                            }
                        });

                        txtSenderRegion.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String sdRegion = txtSenderRegion.getText().toString();
                                if (sdRegion.equals("")) {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                                    txtSenderRegion.setTypeface(customFont);
                                    return;
                                } else {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                                    txtSenderRegion.setTypeface(customFont);
                                }
                            }
                        });

                        receiverFullName.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String rvName = receiverFullName.getText().toString();
                                if (rvName.equals("")) {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                                    receiverFullName.setTypeface(customFont);
                                    return;
                                } else {
//                                    if (receiverFullName.length() > 1) {
//                                        return;
//                                    }
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                                    receiverFullName.setTypeface(customFont);
                                }
                            }
                        });

                        receiverPhone.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String rvPhone = receiverPhone.getText().toString();
                                if (rvPhone.equals("")) {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                                    receiverPhone.setTypeface(customFont);
                                    return;
                                } else {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                                    receiverPhone.setTypeface(customFont);
                                }
                            }
                        });

                        receiverAddress.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String rvAddress = receiverAddress.getText().toString();
                                if (rvAddress.equals("")) {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                                    receiverAddress.setTypeface(customFont);
                                    return;
                                } else {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                                    receiverAddress.setTypeface(customFont);
                                }
                            }
                        });

                        txtReceiverRegion.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String rvRegion = txtReceiverRegion.getText().toString();
                                if (rvRegion.equals("")) {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                                    txtReceiverRegion.setTypeface(customFont);
                                    return;
                                } else {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                                    txtReceiverRegion.setTypeface(customFont);
                                }
                            }
                        });
                        //endregion

                        ivShowHide.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (isShow) {
                                    ivShowHide.setBackgroundResource(R.drawable.arrow_up);
                                    senderPhone.setVisibility(View.VISIBLE);
                                    senderAddress.setVisibility(View.VISIBLE);
                                    txtSenderRegion.setVisibility(View.VISIBLE);
                                    isShow = false;
                                } else {
                                    ivShowHide.setBackgroundResource(R.drawable.arrow_down);
                                    senderPhone.setVisibility(View.GONE);
                                    senderAddress.setVisibility(View.GONE);
                                    txtSenderRegion.setVisibility(View.GONE);
                                    isShow = true;
                                }
                            }
                        });

                        edtWeight.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                try {
                                    double value = Double.parseDouble(editable.toString());
//                                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
//                                    edtWeight.setText(decimalFormat.format(value));
                                    if (idCity == 1) {
                                        for (int i = 0; i < weightProducts.size(); i++) {
                                            WeightProductObj obj = weightProducts.get(i);
                                            if (value <= obj.getNumber()) {
                                                surcharge.setText(CmmFunc.formatMoney(Math.round(obj.getFee())));
                                                break;
                                            } else if (value > 40) {
                                                Toast.makeText(getContext(), R.string.err_45_kg, Toast.LENGTH_SHORT).show();
                                                edtWeight.setText("");
                                                surcharge.setText("");
                                            }
                                        }
                                    } else {
                                        for (int i = 0; i < weightProductPosts.size(); i++) {
                                            WeightProductObj obj = weightProductPosts.get(i);
                                            if (value <= obj.getNumber()) {
                                                surcharge.setText(CmmFunc.formatMoney(Math.round(obj.getFee())));
                                                break;
                                            } else if (value > 5) {
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        edtWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                if (i == EditorInfo.IME_ACTION_DONE) {
                                    DecimalFormat df = new DecimalFormat("##.#");
                                    double value = Double.parseDouble(edtWeight.getText().toString());
                                    edtWeight.setText(df.format(value));
                                }
                                return false;
                            }
                        });

                        commodityFee.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                try {
                                    insuranceFee.setText("1,000");
                                    String value = editable.toString();
                                    value = value.replaceAll(",", "");
                                    int realValue = Math.round(Float.parseFloat(value));
                                    if (realValue > 15000000) {
                                        Toast.makeText(getContext(), R.string.err_15000k, Toast.LENGTH_SHORT).show();
                                        keyBoardShow = new Runnable() {
                                            @Override
                                            public void run() {
                                                insuranceFee.setText("");
                                                commodityFee.setText("");
                                                commodityFee.requestFocus();
                                            }
                                        };

                                        return;
                                    }
                                    insuranceFee.setText(CmmFunc.formatMoney(Math.round(realValue * protectFeePercent * 0.01)));
                                } catch (Exception e) {
                                    insuranceFee.setText("");
                                }

                            }
                        });

                        note.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                String orderNote = note.getText().toString();
                                if (orderNote.equals("")) {
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                                    note.setTypeface(customFont);
                                    return;
                                } else {
                                    if (note.length() > 1) {
                                        return;
                                    }
                                    Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                                    note.setTypeface(customFont);
                                }
                            }
                        });

                        commodityFee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (!hasFocus) {
                                    String strFee = commodityFee.getText().toString().trim();
                                    double fee = stringToDouble(strFee);

                                    fee = calculateRound(fee);
                                    //int roundFee = fee/1000;
                                    if (fee > 15000000 || fee < 0) {
                                        Toast.makeText(getContext(), getString(R.string.err_limit_product_fee), Toast.LENGTH_SHORT).show();
                                        keyBoardShow = new Runnable() {
                                            @Override
                                            public void run() {
                                                commodityFee.setText("");
                                                insuranceFee.setText("");
                                                commodityFee.requestFocus();
                                            }
                                        };
                                        return;
                                    }
                                    double insurance = fee * protectFeePercent * 0.01;
                                    insurance = calculateRound(insurance);
                                    strFee = CmmFunc.formatMoney(Math.round(fee));
                                    String strInsurance = CmmFunc.formatMoney(Math.round(insurance));
                                    commodityFee.setText(strFee);
                                    insuranceFee.setText(strInsurance);
                                }
                            }
                        });

                        codMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (!hasFocus) {
                                    String strFee = codMoney.getText().toString().trim();
                                    double fee = stringToDouble(strFee);
                                    fee = calculateRound(fee);
                                    if (fee > 15000000) {
                                        Toast.makeText(getContext(), getString(R.string.err_limit_cod), Toast.LENGTH_SHORT).show();
                                        codMoney.setText("");
                                        return;
                                    }
                                    strFee = CmmFunc.formatMoney(Math.round(fee));
                                    codMoney.setText(strFee);
                                }
                            }
                        });

                        keyBoardHide = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    commodityFee.clearFocus();
                                    codMoney.clearFocus();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        chkChangeCommodity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b) {
                                    addOn = 3;
                                } else {
                                    addOn = 0;
                                }
                            }
                        });

                        chkSeeCommodity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b) {
                                    seeCommodity = 1;
                                } else {
                                    seeCommodity = 0;
                                }
                            }
                        });

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                                        R.array.payer_array, R.layout.row_spinner_region);
                                adapter.setDropDownViewResource(R.layout.row_spinner_region);
                                payTransport.setAdapter(adapter);

                                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                                        R.array.normal_fast, R.layout.row_spinner_region);
                                adapter.setDropDownViewResource(R.layout.row_spinner_region);
                                spnNormalFast.setAdapter(adapter1);

                                if (getArguments().getInt("type") == 0) {
                                    new ActionGetCity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    new ActionGetConfig().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            }
                        });

                        isLoad = true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threadInit.start();
            //endregion
        }
    }
    //endregion

    //region Override

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == CAPTURE)
                onCapture(data);
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.service_transport:
                try {
                    FormDeliveryObj bean = (FormDeliveryObj) adapterView.getSelectedItem();
                    boolean activeTime = bean.isActive_time();
                    boolean isViettelPost = bean.is_viettel_post();
                    boolean activeEms = bean.isActive_ems();
                    boolean allowSeeOrder = bean.isAllow_see_order();
                    if (activeEms) {
                        llNormalFast.setVisibility(View.GONE);
                    } else {
                        llNormalFast.setVisibility(View.GONE);
                    }
                    if (allowSeeOrder) {
                        chkSeeCommodity.setVisibility(View.VISIBLE);
                    } else {
                        chkSeeCommodity.setVisibility(View.GONE);
                    }
                    if (bean.isIs_return_product()) {
                        chkChangeCommodity.setVisibility(View.VISIBLE);
                    } else {
                        chkChangeCommodity.setVisibility(View.GONE);
                    }
                    idFormDelivery = ((FormDeliveryObj) serviceTransport.getSelectedItem()).getForm_deliver_id();
                    if (activeTime) {
                        //Title người trả phí
                        txtUserPay.setText(getString(R.string.service_charge));
                        llTransport.setVisibility(View.VISIBLE);
                        llnameWeight.setVisibility(View.VISIBLE);
                        llPhotoNote.setVisibility(View.GONE);
                        llTransportService.setVisibility(View.VISIBLE);
                        llViettelPost.setVisibility(View.GONE);
                        codMoney.setEnabled(true);
                        codMoney.setHint("0");
                        getView().findViewById(R.id.txt_vnd).setVisibility(View.VISIBLE);
                        rdoVehicle.setChecked(false);
                        rdoViettel.setChecked(false);
                        serviceTransport.setEnabled(true);
                        view.findViewById(R.id.time_container).setVisibility(View.VISIBLE);
                        shippingFee.setText(CmmFunc.formatMoney(Math.round(bean.getShipping_fee())) + " VND");
                        //Khi activeTime = true, get lấy id form delivery đầu tiên trong list
//                        idFormDelivery = ((FormDeliveryObj) serviceTransport.getSelectedItem()).getForm_deliver_id();
                        chkChangeCommodity.setVisibility(View.VISIBLE);
                        //Kiểm tra nếu tỉnh thành là HCM thì lấy danh sách khối lượng weight_product, ngoại thành thì lấy danh sách weight_product_post

                    } else {
                        view.findViewById(R.id.time_container).setVisibility(View.GONE);
                        //Title người trả phí
                        txtUserPay.setText(getString(R.string.mediate_charge));
//                        chkChangeCommodity.setVisibility(View.GONE);
                        llTransport.setVisibility(View.VISIBLE);
                        llnameWeight.setVisibility(View.VISIBLE);
                        llPhotoNote.setVisibility(View.GONE);
                        //Big update, không dùng check box ở vị trí này, kt lại khi goi API mới
                        llTransportService.setVisibility(View.VISIBLE);
                        llViettelPost.setVisibility(View.GONE);
                        rdoVehicle.setChecked(true);
                        //Dịch vụ vận chuyển ngoại tỉnh
                        int formDelivery = jsonObjectUpdate.getInt("form_delivery");
                        if (formDelivery != 0) {
                            if (formDelivery == 6) {
                                rdoVehicle.setChecked(false);
                                rdoViettel.setChecked(true);
                                isViettelPost = true;
                            } else if (formDelivery == 7) {
                                rdoVehicle.setChecked(true);
                                rdoViettel.setChecked(false);
                            }
                        }
                        serviceTransport.setEnabled(true);
                        shippingFee.setText("");
                        shippingFee.setText(CmmFunc.formatMoney(Math.round(bean.getShipping_fee())));
                    }
                    //Kiểm tra biến boolean viettel_post và khác tp hcm để hiển thị danh sách khối lượng phù hợp
                    if (isViettelPost && jsonObjectUpdate != null && jsonObjectUpdate.getInt("receiver_city") != 1) {
                        WeightProductAdapter weightProductAdapter = new WeightProductAdapter(getContext(),
                                getActivity(), R.layout.row_spinner_region, weightProductPosts);
                        weight.setAdapter(weightProductAdapter);
                        if (getArguments().getInt("type") != 0) {
                            int idProductWeight = jsonObjectUpdate.getInt("product_weight");
                            for (int j = 0; j < weightProducts.size(); j++) {
                                if (weightProducts.get(j).getId() == idProductWeight) {
                                    weight.setSelection(j);
                                }
                            }
                        }
                    } else {
                        WeightProductAdapter weightProductAdapter = new WeightProductAdapter(getContext(),
                                getActivity(), R.layout.row_spinner_region, weightProducts);
                        weight.setAdapter(weightProductAdapter);
                        if (getArguments().getInt("type") != 0) {
                            int idProductWeight = jsonObjectUpdate.getInt("product_weight");
                            for (int j = 0; j < weightProducts.size(); j++) {
                                if (weightProducts.get(j).getId() == idProductWeight) {
                                    weight.setSelection(j);
                                }
                            }
                        }
                    }
                    edtWeight.setText(jsonObjectUpdate.getInt("product_weight_kg") + "");

                    AddressCreateOrderAdapter addressCreateOrderAdapter = new AddressCreateOrderAdapter(getContext(), (FragmentActivity) getContext(), R.layout.row_spinner_region,
                            CreateOrderFragment.this, shopAddressObjs);
                    spnAddress.setAdapter(addressCreateOrderAdapter);
                    String addressName = jsonObjectUpdate.getString("sender_fullname");
                    for (int j = 0; j < shopAddressObjs.size(); j++) {
                        if (shopAddressObjs.get(j).getName().equals(addressName)) {
                            spnAddress.setSelection(j);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.weight:
                //Ẩn hiện thông báo khi user chọn khối lượng lớn hơn 3kg
                if (weight.getSelectedItemPosition() > 0) {
                    txtAlertWeight.setVisibility(View.VISIBLE);
                } else {
                    txtAlertWeight.setVisibility(View.GONE);
                }
                break;
            case R.id.spn_address:
                ShopAddressObj obj = (ShopAddressObj) adapterView.getSelectedItem();
                senderPhone.setText(obj.getPhone());
                senderAddress.setText(obj.getAddress());
                if (obj.getWard_name().equals("")) {
                    txtSenderRegion.setText(obj.getCity_name() + " - " + obj.getDistrict_name());
                } else {
                    txtSenderRegion.setText(obj.getCity_name() + " - " + obj.getDistrict_name() + " - " + obj.getWard_name());
                }
                idSenderCity = obj.getCity_id();
                idSenderDistrict = obj.getDistrict_id();
                idSenderWard = obj.getWard_id();
                idAddress = obj.getId_address();
                StorageHelper.set(StorageHelper.ID_ADDRESS, idAddress + "");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //Sender region
        if (RegionDialogFragment.regionObjSenderList.size() != 3) {
            RegionDialogFragment.regionObjSenderList.clear();
            if (txtSenderRegion != null) {
//                txtSenderRegion.setText("");
            }
        } else {
//            String senderAddress = "";
//            idSenderCity = 0;
//            idSenderDistrict = 0;
//            idSenderWard = 0;

            //lấy tên tỉnh thành theo: Thành phố - Phường - Quận
//            for (int i = 0; i < RegionDialogFragment.regionObjSenderList.size(); i++) {
//                senderAddress += RegionDialogFragment.regionObjSenderList.get(i).getName() + " - ";
//            }

            //For lấy tỉnh thành theo Phường - Quận - Thành phố
//            for (int i = RegionDialogFragment.regionObjSenderList.size() - 1; i >= 0; i--) {
//                senderAddress += RegionDialogFragment.regionObjSenderList.get(i).getName() + " - ";
//            }
//            senderAddress = senderAddress.substring(0, senderAddress.length() - 3);
//            txtSenderRegion.setText(senderAddress);
//            idSenderCity = RegionDialogFragment.regionObjSenderList.get(0).getId();
//            idSenderDistrict = RegionDialogFragment.regionObjSenderList.get(1).getId();
//            idSenderWard = RegionDialogFragment.regionObjSenderList.get(2).getId();
        }

        //Receiver region
        if (RegionDialogFragment.regionObjList.size() != 3) {
            RegionDialogFragment.regionObjList.clear();
            if (txtReceiverRegion == null) {
                return;
            }
            txtReceiverRegion.setText("");
            return;
        }
        String address = StringUtils.EMPTY;
        idCity = 0;
        idDistrict = 0;
        idWard = 0;

        idCity = RegionDialogFragment.regionObjList.get(0).getId();
        idDistrict = RegionDialogFragment.regionObjList.get(1).getId();
        idWard = RegionDialogFragment.regionObjList.get(2).getId();

        //Lấy tên tỉnh thành theo: Thành phố - Phường - Quận
        for (int i = 0; i < RegionDialogFragment.regionObjList.size(); i++) {
            RegionObj regionObj = RegionDialogFragment.regionObjList.get(i);
            if (regionObj.getId() != 0) {
                address += regionObj.getName() + " - ";
            }
        }
        //For lấy tên tỉnh thành theo: Phường - Quận - Thành phố
//        for (int i = RegionDialogFragment.regionObjList.size() - 1; i >= 0; i--) {
//            address += RegionDialogFragment.regionObjList.get(i).getName() + " - ";
//        }
        address = address.substring(0, address.length() - 3);
        txtReceiverRegion.setText(address);
        new ActionGetFormDelivery().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity, idDistrict);
    }

    //endregion

    //region Methods

    //Làm tròn thành định dạng 1000
    private double calculateRound(double fee) {
        long round = (long) (fee / 1000);
        double nem = fee % 1000;
        if (nem > 0) {
            round = round + 1;
        }
        double value = round * 1000;
        return value;
    }

    //Chuyển String thành double
    private double stringToDouble(String value) {
        if (value.equals(""))
            return 0;
        if (value != "" || value != null) {
            double valueDouble;
            value = value.replaceAll(",", "");
            valueDouble = Double.parseDouble(value);
            return valueDouble;
        }
        return 0.0;
    }

    //region Định dạng text hiển thị số tiền
//    private double formatMoney(String value) {
//        try {
//            if (value.equals("")) {
//                return 0;
//            }
//            return Double.parseDouble(value.replaceAll("\\.", "").replaceAll("VND", "").trim());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
    private double formatMoney(String value) {
        try {
            if (value.equals("")) {
                return 0;
            }
            return Double.parseDouble(value.replaceAll(",", "").replaceAll("VND", "").trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    //endregion

    //Hiển thị thời gian
    private String getTime(DateTime dateTime) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        return StringUtils.leftPad(dateTime.getHourOfDay() + "", 2, "0") + "h" +
                StringUtils.leftPad(dateTime.getMinuteOfHour() + "", 2, "0");
    }

    //Hiển thị ngày
    private String getDate(DateTime dateTime) {
        if (dateTime == null) {
            return StringUtils.EMPTY;
        }
        return StringUtils.leftPad(dateTime.getDayOfMonth() + "", 2, "0") + " - " +
                StringUtils.leftPad(dateTime.getMonthOfYear() + "", 2, "0") + " - " + dateTime.getYear();
    }

    //Định dạng ngày (year-month-day-hour-minute)
    private String formatDate(int year, int month, int day, int hour, int minute) {
        String value = StringUtils.leftPad(hour + "", 2, "0") + ":" +
                StringUtils.leftPad(minute + "", 2, "0") + ":" + "00" + " " + StringUtils.leftPad(day + "", 2, "0") + "-" +
                StringUtils.leftPad(month + "", 2, "0") + "-" + year;
        return value;
    }

    //Định dạng ngày (year-month-day)
    private String formatDate(int year, int month, int day) {
        String value = year + "-" + StringUtils.leftPad(month + "", 2, "0") + "-" +
                StringUtils.leftPad(day + "", 2, "0");
        return value;
    }

    //Dialog from time
    private void fromTimeDialog() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                try {
                    if (hourOfDay > 21 || hourOfDay < 9) {
                        //thong bao
                        Toast.makeText(getContext(), getString(R.string.err_office_time), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //Temporary var
                    DateTime temp = new DateTime(from.getYear(), from.getMonthOfYear(), from.getDayOfMonth(), hourOfDay, minute);
                    DateTime toDate = new DateTime();
                    if (temp.getMillis() < toDate.getMillis()) {
                        Toast.makeText(getContext(), getString(R.string.err_from_to_time), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (to.getMillis() - temp.getMillis() < 3600000) {
                        Toast.makeText(getContext(), getString(R.string.err_60_minites), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    from = temp;
                    fromTime.setText(getTime(from));
//                    int idCity = RegionDialogFragment.regionObjList.get(0).getId();
//                    int idDistrict = RegionDialogFragment.regionObjList.get(1).getId();
//                    new ActionCalculator().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity, idDistrict, from, to);
//                    new ActionGetFormDelivery().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity, idDistrict);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, from.getHourOfDay(), from.getMinuteOfHour(), true);
        //minuteInterval: khoảng cách số phút chọn
        timePickerDialog.setTimeInterval(1, 60);
        timePickerDialog.show(getActivity().getFragmentManager(), timePickerDialog.getClass().getName());
    }

    //Dialog to time
    private void toTimeDialog() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                if (hourOfDay > 21 || hourOfDay < 9) {
                    Toast.makeText(getContext(), getString(R.string.err_office_time), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hourOfDay == 21 || hourOfDay == 9) {
                    if (minute > 0) {
                        Toast.makeText(getContext(), getString(R.string.err_office_time), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String endTime = formatDate(to.getYear(), to.getMonthOfYear(), to.getDayOfMonth(),
                        hourOfDay, minute);
                //Temporary var
                DateTime temp = DateTime.parse(endTime, df);
                if (temp.getMillis() < from.getMillis()) {
                    //thong bao nho hon fromTIme
                    Toast.makeText(getContext(), getString(R.string.err_from_to), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (temp.getMillis() - from.getMillis() < 3600000) {
                    Toast.makeText(getContext(), getString(R.string.err_60_minites), Toast.LENGTH_SHORT).show();
                    return;
                }
                to = temp;
                toTime.setText(getTime(to));
//                int idCity = RegionDialogFragment.regionObjList.get(0).getId();
//                int idDistrict = RegionDialogFragment.regionObjList.get(1).getId();
//                new ActionCalculator().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity, idDistrict, from, to);
//                new ActionGetFormDelivery().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity, idDistrict);
            }
        }, to.getHourOfDay(), to.getMinuteOfHour(), true);
        //minuteInterval: khoảng cách số phút chọn
        timePickerDialog.setTimeInterval(1, 60);
        timePickerDialog.show(getActivity().getFragmentManager(), timePickerDialog.getClass().getName());
    }

    //Dialog to date
    private void toDateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        //today - Ngày hiện tại
                        DateTime today = new DateTime();

                        //temp - Biến tạm cho ngày tháng người dùng chọn trên picker
                        DateTime temp = new DateTime(year, monthOfYear + 1, dayOfMonth, from.getHourOfDay(), from.getMinuteOfHour());

                        //tempToday - Biến tạm ngày hiện tại với ngày tháng hiện tại và giờ
                        DateTime tempToday = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), from.getHourOfDay(),
                                from.getMinuteOfHour());
                        //Ngày được chọn nhỏ hơn ngày hiện tại
                        if (temp.getMillis() < tempToday.getMillis()) {
                            Toast.makeText(getContext(), getString(R.string.err_date_from), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Ngày được chọn bằng ngày hiện tại
                        if (temp.getMillis() == tempToday.getMillis()) {
                            if (temp.getMillis() < today.getMillis()) {
                                temp = roundTime(today, 900000);
                            }
                            from = temp;
                            to = new DateTime(year, monthOfYear + 1, dayOfMonth, 21, 00);
                        } else {
                            from = new DateTime(year, monthOfYear + 1, dayOfMonth, from.getHourOfDay(), from.getMinuteOfHour());
                            to = new DateTime(year, monthOfYear + 1, dayOfMonth, to.getHourOfDay(), to.getMinuteOfHour());
                        }
//                        fromTime.setText(getTime(from));
                        toTime.setText(getTime(to));
                        toDate.setText(getDate(to));
//                        int idCity = RegionDialogFragment.regionObjList.get(0).getId();
//                        int idDistrict = RegionDialogFragment.regionObjList.get(1).getId();
//                        new ActionCalculator().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity, idDistrict, from, to);
//                        new ActionGetFormDelivery().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity, idDistrict);
                    }
                }, to.getYear(), to.getMonthOfYear() - 1, to.getDayOfMonth());

        datePickerDialog.show();
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_library),
                getString(R.string.photo_cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.add_photo));
        final AlertDialog.Builder builder1 = builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getContext());
                if (items[item].equals(getString(R.string.take_photo))) {
                    //userChoosenTask = getString(R.string.take_photo);
                    if (result) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUri = getActivity().getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                        startActivityForResult(intent, PICTURE_RESULT);

                        startActivityForResult(intent, CAPTURE);
                    }
                } else if (items[item].equals(getString(R.string.choose_from_library))) {
                    //userChoosenTask = getString(R.string.choose_from_library);
                    if (result) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY);
                    }
                } else if (items[item].equals(getString(R.string.photo_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //set hình ảnh vào ImageView
    private void setImage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    upload1.setImageResource(R.drawable.ic_upload_photo);
                    upload2.setImageResource(R.drawable.ic_upload_photo);
                    upload3.setImageResource(R.drawable.ic_upload_photo);
                    int size = files.size();
                    if (size > 0) {
                        removeUpload1.setVisibility(View.VISIBLE);
                        File file = files.get(0);
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        upload1.setImageBitmap(bitmap);
                    }
                    if (size > 1) {
                        removeUpload2.setVisibility(View.VISIBLE);
                        File file = files.get(1);
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        upload2.setImageBitmap(bitmap);
                    }
                    if (size > 2) {
                        removeUpload3.setVisibility(View.VISIBLE);
                        File file = files.get(2);
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        upload3.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Chụp ảnh từ camera
    private void onCaptureImageResult(Intent data) {
        try {
            Bitmap bmp;
            if (data.getData() == null) {
                bmp = (Bitmap) data.getExtras().get("data");
            } else {
                String fullPath;
                Uri uri = Uri.parse(data.getDataString());
                fullPath = CmmFunc.getPathFromUri(getActivity(), uri);
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bmp = BitmapFactory.decodeStream(bufferedInputStream);
            }
            File file = CmmFunc.persistImage(getContext(), bmp);
            if (files.size() < 3) {
                files.add(file);
                setImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCapture(Intent data) {
        try {
            Bitmap bmp = null;
            if (imageUri != null) {
                String realPath = CmmFunc.getPathFromUri(getActivity(), imageUri);
                if (realPath != null) {
                    ExifInterface exif = new ExifInterface(realPath);
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    switch (rotation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotation = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotation = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotation = 270;
                            break;
                        default:
                            rotation = 0;
                            break;
                    }
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotation);
                    }

                    bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                } else {
                    if (data.getData() == null) {
                        bmp = (Bitmap) data.getExtras().get("data");
                    } else {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    }
                }
            } else {
                if (data.getData() == null) {
                    bmp = (Bitmap) data.getExtras().get("data");
                } else {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    bmp = BitmapFactory.decodeStream(bufferedInputStream);
                }
            }

            Bitmap bmpResize = CmmFunc.scaleDown(bmp, 720, true);
            File file = CmmFunc.bitmapToFile(getActivity(), bmpResize);
            if (files.size() < 3) {
                files.add(file);
                setImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    //Chọn ảnh từ thư viện ảnh
    private void onSelectFromGalleryResult(Intent data) {
        try {
            Bitmap bmp = null;
            //Load hình ảnh từ đường dẫn thật, điều chỉnh khi hiển thị là
            Uri uri = Uri.parse(data.getDataString());
            if (uri != null) {
                String realPath = CmmFunc.getPathFromUri(getActivity(), uri);
                if (realPath != null) {
                    ExifInterface exif = new ExifInterface(realPath);
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    switch (rotation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotation = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotation = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotation = 270;
                            break;
                        default:
                            rotation = 0;
                            break;
                    }
                    Matrix matrix = new Matrix();
                    if (rotation != 0f) {
                        matrix.preRotate(rotation);
                    }

                    bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
                } else {
                    if (data.getData() == null) {
                        bmp = (Bitmap) data.getExtras().get("data");
                    } else {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        bmp = BitmapFactory.decodeStream(bufferedInputStream);
                    }
                }
            } else {
                if (data.getData() == null) {
                    bmp = (Bitmap) data.getExtras().get("data");
                } else {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    bmp = BitmapFactory.decodeStream(bufferedInputStream);
                }
            }
            Bitmap bmpResize = CmmFunc.scaleDown(bmp, 720, true);
            File file = CmmFunc.bitmapToFile(getActivity(), bmpResize);
            if (files.size() < 3) {
                files.add(file);
                setImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Khi bấm nút tạo đơn hàng
    private void confirm() {
        confirm.setOnClickListener(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    confirm.setOnClickListener(CreateOrderFragment.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        CmmFunc.hideKeyboard(getActivity());

        boolean isSuccess = true;
        if (senderPhone.getText().toString().equals("")) {
            senderPhone.setError(getString(R.string.err_sender_phone));
            senderPhone.requestFocus();
            isSuccess = false;
        }
        if (senderAddress.getText().toString().equals("")) {
            senderAddress.setError(getString(R.string.err_sender_address));
            senderAddress.requestFocus();
            isSuccess = false;
        }
        if (txtSenderRegion.getText().toString().equals("")) {
            txtSenderRegion.setError(getString(R.string.err_sender_region));
            txtSenderRegion.requestFocus();
            isSuccess = false;
        }
        if (receiverFullName.getText().toString().equals("")) {
            receiverFullName.setError(getString(R.string.err_receiver_name));
            receiverFullName.requestFocus();
            isSuccess = false;
        }
        if (receiverPhone.getText().toString().equals("")) {
            receiverPhone.setError(getString(R.string.err_receiver_phone));
            receiverPhone.requestFocus();
            isSuccess = false;
        }
        if (receiverAddress.getText().toString().equals("")) {
            receiverAddress.setError(getString(R.string.err_address_receiver));
            receiverAddress.requestFocus();
            isSuccess = false;
        }
        if (txtReceiverRegion.getText().toString().equals("")) {
            txtReceiverRegion.setError(getString(R.string.err_receiver_region));
            txtReceiverRegion.requestFocus();
            Toast.makeText(getContext(), getString(R.string.err_receiver_region), Toast.LENGTH_SHORT).show();
            isSuccess = false;
        }
        if (edtWeight.getText().toString().equals("")) {
            edtWeight.setError(getString(R.string.err_weight));
            edtWeight.requestFocus();
            isSuccess = false;
        }
        if (chkChangeCommodity.isChecked()) {

        }
        if (!isSuccess) {
            return;
        }

        String strReciverFullName = receiverFullName.getText().toString();
        String strReciverPhone = receiverPhone.getText().toString();
        String strReciverAddress = receiverAddress.getText().toString();
        String strSenderFullName = ((ShopAddressObj) spnAddress.getSelectedItem()).getName();
        String strSenderPhone = senderPhone.getText().toString();
        String strSenderAddress = senderAddress.getText().toString();
        idSenderCity = ((ShopAddressObj) spnAddress.getSelectedItem()).getCity_id();
        idSenderDistrict = ((ShopAddressObj) spnAddress.getSelectedItem()).getDistrict_id();
        idSenderWard = ((ShopAddressObj) spnAddress.getSelectedItem()).getWard_id();
        int payer = payTransport.getSelectedItemPosition() + 1;
        String productName = commodityName.getText().toString();
        double weight;
        if (edtWeight.getText().toString().equals("")) {
            weight = 1.0;
        } else {
            weight = Double.parseDouble(edtWeight.getText().toString());
        }
        if (idCity == 1) {
            if (Double.parseDouble(edtWeight.getText().toString()) > 45) {
                Toast.makeText(getContext(), R.string.err_45_kg, Toast.LENGTH_SHORT).show();
                edtWeight.setText("");
                surcharge.setText("");
                return;
            }
        }
        if (idCity != 1) {
            if (Double.parseDouble(edtWeight.getText().toString()) > 5) {
                Toast.makeText(getContext(), R.string.err_5_kg, Toast.LENGTH_SHORT).show();
                edtWeight.setText("");
                surcharge.setText("");
                return;
            }
        }

        double productValue = formatMoney(commodityFee.getText().toString());
        String comment = note.getText().toString();
        String shopOrderCode = edtShopOrderCode.getText().toString();
        double cod = formatMoney(codMoney.getText().toString());
        if (getArguments().getInt("type") == 0 || getArguments().getInt("type") == 2) {
            typeAction = 0;
        } else {
            typeAction = 1;
        }
        new ActionCreate().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                strSenderFullName, strSenderPhone, strSenderAddress, idSenderCity, idSenderDistrict, idSenderWard, strReciverFullName, strReciverPhone,
                strReciverAddress, idCity, idDistrict, idWard, payer, productName,
//                ((WeightProductObj) weight.getSelectedItem()).getId(),
                weight, idFormDelivery, addOn, productValue, comment, cod, 0.0, typeAction, orderCode, seeCommodity, shopOrderCode);
    }

    //Làm tròn số phút thành 00/15/30/45 để tránh trường hợp giờ gửi lên nhỏ hơn thời gian trên server.
    public DateTime roundTime(DateTime dateTime, long miliRound) {
        long mili = dateTime.getMillis();
        long temp = (mili / miliRound) * miliRound + miliRound;
        return new DateTime(temp);
    }

    //Get số điện thoại từ danh bạ rồi set vào edittext số điện thoại người nhận
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            Uri uri = data.getData();
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();

            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            receiverPhone.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //endregion

    //region Events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                CmmFunc.pop(getActivity());
                break;
            case R.id.txt_receiver_region:
                userRegion = 2;
                RegionDialogFragment.regionObjList.clear();
                CmmFunc.addFragment(getActivity(), R.id.main_container, RegionDialogFragment.newInstance(2, 0, 0, 0));
                txtReceiverRegion.setError(null);
                hideKeyboard();
                break;
            case R.id.from_time:
                fromTimeDialog();
                break;
            case R.id.to_time:
                toTimeDialog();
                break;
            case R.id.to_date:
                toDateDialog();
                break;
            case R.id.upload_1:
                if (files.size() > 0) {
                    //xem ảnh
                    try {
                        File file = files.get(0);
                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(byteArray));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    selectImage();
                }
                break;
            case R.id.upload_2:
                if (files.size() > 1) {
                    try {
                        File file = files.get(1);
                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(byteArray));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    selectImage();
                }
                break;
            case R.id.upload_3:
                if (files.size() > 2) {
                    try {
                        File file = files.get(2);
                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        CmmFunc.addFragment(getActivity(), R.id.main_container, ShowPhotoFragment.newInstance(byteArray));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    selectImage();
                }
                break;
            case R.id.remove_upload_1:
                if (files.size() > 0) {
                    files.remove(0);
                    setImage();
                    removeUpload1.setVisibility(View.GONE);
                }
                removeUpload1.setVisibility(View.GONE);
                removeUpload2.setVisibility(View.GONE);
                removeUpload3.setVisibility(View.GONE);
                if (files.size() > 0) {
                    removeUpload1.setVisibility(View.VISIBLE);
                }
                if (files.size() > 1) {
                    removeUpload2.setVisibility(View.VISIBLE);
                }
                if (files.size() > 2) {
                    removeUpload3.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.remove_upload_2:
                if (files.size() > 1) {
                    files.remove(1);
                    setImage();
                    removeUpload2.setVisibility(View.GONE);
                }
                removeUpload1.setVisibility(View.GONE);
                removeUpload2.setVisibility(View.GONE);
                removeUpload3.setVisibility(View.GONE);
                if (files.size() > 0) {
                    removeUpload1.setVisibility(View.VISIBLE);
                }
                if (files.size() > 1) {
                    removeUpload2.setVisibility(View.VISIBLE);
                }
                if (files.size() > 2) {
                    removeUpload3.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.remove_upload_3:
                if (files.size() > 2) {
                    files.remove(2);
                    setImage();
                    removeUpload3.setVisibility(View.GONE);
                }
                removeUpload1.setVisibility(View.GONE);
                removeUpload2.setVisibility(View.GONE);
                removeUpload3.setVisibility(View.GONE);
                if (files.size() > 0) {
                    removeUpload1.setVisibility(View.VISIBLE);
                }
                if (files.size() > 1) {
                    removeUpload2.setVisibility(View.VISIBLE);
                }
                if (files.size() > 2) {
                    removeUpload3.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.confirm:
                confirm();
                break;
            case R.id.iv_contacts:
                Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactIntent, RESULT_PICK_CONTACT);
                break;
        }
    }

    //onCheckedChanged for RadioButton - dùng cho RadioButton
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rdo_vehicle:
                if (b) {
                    FormDeliveryObj obj = formDelivery.get(0);
//                    idFormDelivery = obj.getForm_deliver_id();
                    llNormalFast.setVisibility(View.GONE);
                    codMoney.setHint(getString(R.string.not_available));
                    getView().findViewById(R.id.txt_vnd).setVisibility(View.GONE);
                    codMoney.setEnabled(true);
                    shippingFee.setText(CmmFunc.formatMoney(Math.round(obj.getShipping_fee())) + " VND");
                    //Set danh sách khối lượng khi hình thức vận chuyển là nhà xe
                    WeightProductAdapter weightProductAdapter = new WeightProductAdapter(getContext(),
                            getActivity(), R.layout.row_spinner_region, weightProducts);
                    weight.setAdapter(weightProductAdapter);
                }
                break;
            case R.id.rdo_viettel:
                if (b) {
                    FormDeliveryObj obj = formDelivery.get(1);
//                    idFormDelivery = obj.getForm_deliver_id();
                    codMoney.setHint("0");
                    getView().findViewById(R.id.txt_vnd).setVisibility(View.VISIBLE);
                    codMoney.setHint("0");
                    llNormalFast.setVisibility(View.GONE);
                    codMoney.setEnabled(true);
                    shippingFee.setText(CmmFunc.formatMoney(Math.round(obj.getShipping_fee())) + " VND");
                    //Set danh sách khối lượng khi hình thức vận chuyển là viettel post, so sánh xem có phải tỉnh thành là HCM không
                    if (idCity != 1) {
                        WeightProductAdapter weightProductAdapter = new WeightProductAdapter(getContext(),
                                getActivity(), R.layout.row_spinner_region, weightProductPosts);
                        weight.setAdapter(weightProductAdapter);
                    } else {
                        WeightProductAdapter weightProductAdapter = new WeightProductAdapter(getContext(),
                                getActivity(), R.layout.row_spinner_region, weightProducts);
                        weight.setAdapter(weightProductAdapter);
                    }
                }
                break;
        }
    }
    //endregion

    //region Actions
    //Gọi đầu tiên
    class ActionGetCity extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                defaultSenderDistrictID = -1;
                defaultReceiverDistrictID = -1;
                String value = APIHelper.getRegionAPI(1, 0, 0);
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
                    if (getArguments().getInt("type") == 0) {
                        new ActionGetProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    new ActionGetConfig().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ActionGetProfile extends Async {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.profileAPI(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), StorageHelper.get(StorageHelper.USERNAME),
                        StorageHelper.get(StorageHelper.TOKEN));
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
                    shopAddressObjs = (List<ShopAddressObj>) CmmFunc.tryParseList(data.getString("user_address"), ShopAddressObj.class);
                    AddressCreateOrderAdapter adapterAddress = new AddressCreateOrderAdapter(getContext(), (FragmentActivity) getContext(), R.layout.row_spinner_region,
                            CreateOrderFragment.this, shopAddressObjs);
                    spnAddress.setAdapter(adapterAddress);

                    if (shopAddressObjs.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.err_list_address_empty);
                        builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                            }
                        });
                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        if (((ShopAddressObj) spnAddress.getSelectedItem()).getName().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.err_update_sender_name);
                            builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                                }
                            });
                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        if (((ShopAddressObj) spnAddress.getSelectedItem()).getPhone().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.err_update_sender_phone);
                            builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                                }
                            });
                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        if (((ShopAddressObj) spnAddress.getSelectedItem()).getAddress().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.err_update_sender_address);
                            builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                                }
                            });
                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        if (((ShopAddressObj) spnAddress.getSelectedItem()).getCity_name().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.err_no_region);
                            builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                                }
                            });
                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        if (((ShopAddressObj) spnAddress.getSelectedItem()).getDistrict_name().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(R.string.err_no_region);
                            builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    CmmFunc.replaceFragment(getActivity(), R.id.main_container, ShopInfoFragment.newInstance());
                                }
                            });
                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }

                    if (getArguments().getInt("type") == 0) {
                        if (StorageHelper.get(StorageHelper.ID_ADDRESS) != null) {
                            AddressCreateOrderAdapter addressCreateOrderAdapter = new AddressCreateOrderAdapter(getContext(), (FragmentActivity) getContext(), R.layout.row_spinner_region,
                                    CreateOrderFragment.this, shopAddressObjs);
                            spnAddress.setAdapter(addressCreateOrderAdapter);
                            int id_address = Integer.parseInt(StorageHelper.get(StorageHelper.ID_ADDRESS));
                            for (int j = 0; j < shopAddressObjs.size(); j++) {
                                if (shopAddressObjs.get(j).getId_address() == id_address) {
                                    spnAddress.setSelection(j);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hideProgress();
            }
        }
    }

    //Gọi sau khi lấy được City
    class ActionGetConfig extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.configOrder(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
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
                    protectID = data.getInt("add_on_protect");
                    protectFeePercent = data.getDouble("protect_fee_percent");
                    formServices = (List<FormDeliveryServiceObj>) CmmFunc.tryParseList(data.getString("form_delivery"),
                            FormDeliveryServiceObj.class);
                    //khối lượng
                    weightProducts = (List<WeightProductObj>) CmmFunc.tryParseList(data.getString("weight_product"),
                            WeightProductObj.class);
                    weightProductPosts = (List<WeightProductObj>) CmmFunc.tryParseList(data.getString("weight_product_post"),
                            WeightProductObj.class);

                    if (getArguments().getInt("type") != 0) {
                        if (getArguments().getInt("type") == 1) {
                            titleCreateOrder.setText(getString(R.string.edit_order));
                        }
                        if (getArguments().getInt("type") == 2) {
                            titleCreateOrder.setText(getString(R.string.copy_order));
                        }
                        //Người nhận
                        RegionObj c = new RegionObj();
                        RegionObj d = new RegionObj();
                        RegionObj w = new RegionObj();
                        String fullAddress = jsonObjectUpdate.getString("receiver_full_address");
                        String[] addresses = fullAddress.split(",");

                        if (addresses.length == 4) {
                            c = new RegionObj();
                            c.setId(jsonObjectUpdate.getInt("receiver_city"));
                            if (addresses.length > 3) {
                                c.setName(addresses[3]);
                            }
                            d = new RegionObj();
                            d.setId(jsonObjectUpdate.getInt("receiver_district"));
                            if (addresses.length > 2) {
                                d.setName(addresses[2]);
                            }
                            w = new RegionObj();
                            w.setId(jsonObjectUpdate.getInt("receiver_ward"));
                            if (addresses.length > 1) {
                                w.setName(addresses[1]);
                            }
                        }
                        if (addresses.length == 3) {
                            c = new RegionObj();
                            c.setId(jsonObjectUpdate.getInt("receiver_city"));
                            if (addresses.length > 2) {
                                c.setName(addresses[2]);
                            }
                            d = new RegionObj();
                            d.setId(jsonObjectUpdate.getInt("receiver_district"));
                            if (addresses.length > 1) {
                                d.setName(addresses[1]);
                            }
                            w = new RegionObj();
                            w.setId(jsonObjectUpdate.getInt("receiver_ward"));
                            if (addresses.length > 0) {
                                w.setName(addresses[0]);
                            }
                        }

                        //Tỉnh thành người nhận
                        RegionDialogFragment.regionObjList.clear();
                        RegionDialogFragment.regionObjList.add(c);
                        RegionDialogFragment.regionObjList.add(d);
                        RegionDialogFragment.regionObjList.add(w);

                        if (getArguments().getInt("type") != 0 && jsonObjectUpdate != null) {
                            new ActionGetProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            receiverFullName.setText(jsonObjectUpdate.getString("receiver_fullname"));
                            receiverPhone.setText(jsonObjectUpdate.getString("receiver_phone"));
                            receiverAddress.setText(jsonObjectUpdate.getString("receiver_address"));
                            orderCode = jsonObjectUpdate.getString("order_code");

                            //người trả
                            int payer = jsonObjectUpdate.getInt("payer");
                            payTransport.setSelection(payer - 1);

                            //Gửi nhanh/gửi thường
                            int emsService = jsonObjectUpdate.getInt("ems_service");
                            spnNormalFast.setSelection(emsService - 1);

                            //Cho xem hàng
                            int allowSeeOrder = jsonObjectUpdate.getInt("allow_see_order");
                            if (allowSeeOrder == 1) {
                                chkSeeCommodity.setChecked(true);
                            } else if (allowSeeOrder == 0) {
                                chkSeeCommodity.setChecked(false);
                            }

                            edtWeight.setText(jsonObjectUpdate.getDouble("product_weight_kg") + "");
                            edtShopOrderCode.setText(jsonObjectUpdate.getString("shop_order_code"));

                            //Tiền thu hộ
                            codMoney.setText(CmmFunc.formatMoney(Math.round(jsonObjectUpdate.getDouble("money_cod"))));

                            //Đồi hàng, lấy hàng về (3 or !=3)
                            int addOn = jsonObjectUpdate.getInt("addon");
                            if (addOn == 3) {
                                chkChangeCommodity.setChecked(true);
                            } else {
                                chkChangeCommodity.setChecked(false);
                            }

                            //Dịch vụ bảo hiểm
                            int insuranceService = jsonObjectUpdate.getInt("insurance_service");
                            if (insuranceService == 0) {
                            } else if (insuranceService == 1) {
                                commodityFee.setText(CmmFunc.formatMoney(Math.round(jsonObjectUpdate.getDouble("product_value"))));
                                commodityFee.clearFocus();
                            }

                            commodityName.setText(jsonObjectUpdate.getString("product_type"));
                            note.setText(jsonObjectUpdate.getString("note"));

                            RegionDialogFragment.regionObjList.clear();
                            RegionDialogFragment.regionObjList.add(c);
                            RegionDialogFragment.regionObjList.add(d);
                            RegionDialogFragment.regionObjList.add(w);
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String str = jsonObjectUpdate.getString("images");
                                    JSONArray images = new JSONArray(str);
                                    for (int i = 0; i < images.length(); i++) {
                                        final String URL = images.getString(i);
                                        Bitmap bmp = CmmFunc.getBitmapFromURL(URL);
                                        File file = CmmFunc.persistImage(getContext(), bmp);
                                        files.add(file);
                                    }
                                    setImage();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    //Cẩn thận, cái này gọi khi load man hình
                    onResume();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Gọi khi city thay đổi
    class ActionGetFormDelivery extends Async {
        boolean isTime;

        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = null;
                int idreceiverCity = (int) objects[0];
                int idReceiverDistrict = (int) objects[1];
                isTime = false;
//                DateTime fromTime = (DateTime) objects[2];
//                DateTime toTime = (DateTime) objects[3];
//                String startTime = formatDate(fromTime.getYear(), fromTime.getMonthOfYear(), fromTime.getDayOfMonth(), fromTime.getHourOfDay(), fromTime.getMinuteOfHour());
//                String endTime = formatDate(toTime.getYear(), toTime.getMonthOfYear(), toTime.getDayOfMonth(), toTime.getHourOfDay(), toTime.getMinuteOfHour());
                value = APIHelper.getFormDelivery(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN),
                        idreceiverCity, idReceiverDistrict);
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
                    llTransport.setVisibility(View.VISIBLE);
                    llnameWeight.setVisibility(View.VISIBLE);
                    llPhotoNote.setVisibility(View.GONE);
                    llTransportService.setVisibility(View.VISIBLE);

                    formDelivery = (List<FormDeliveryObj>) CmmFunc.tryParseList(data.getString("form_delivery"),
                            FormDeliveryObj.class);

                    FormDeliveryAdapter formDeliveryAdapter = new FormDeliveryAdapter(getContext(),
                            getActivity(), R.layout.row_spinner_region, formDelivery);
                    serviceTransport.setAdapter(formDeliveryAdapter);

                    if (!isTime) {
                        FormDeliveryObj formDeliveryObj = formDelivery.get(0);
                        String strFrom = formDeliveryObj.getTime_from_default() + " " + formDeliveryObj.getDate_from_default();
                        String strTo = formDeliveryObj.getTime_to_default() + " " + formDeliveryObj.getDate_to_default();
                        from = DateTime.parse(strFrom, df);
                        to = DateTime.parse(strTo, df);
                        fromTime.setText(getTime(from));
                        toTime.setText(getTime(to));
                        fromDate.setText(getDate(from));
                        toDate.setText(getDate(from));
                    }

                    if (getArguments().getInt("type") != 0) {
                        int updateFormDelivery = jsonObjectUpdate.getInt("form_delivery");
                        for (int j = 0; j < formDelivery.size(); j++) {
                            if (formDelivery.get(j).getForm_deliver_id() == updateFormDelivery) {
                                serviceTransport.setSelection(j);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ActionCreate extends Async {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String strSenderFullName = (String) objects[0];
                String strSenderPhone = (String) objects[1];
                String strSenderAddress = (String) objects[2];
                int idSenderCity = (int) objects[3];
                int idSenderDistrict = (int) objects[4];
                int idSenderWard = (int) objects[5];

                String strReceiveFullName = (String) objects[6];
                String strReceivePhone = (String) objects[7];
                String strReceiveAddress = (String) objects[8];
                int idReceiveCity = (int) objects[9];
                int idReceiveDistrict = (int) objects[10];
                int idReceiveWard = (int) objects[11];

                int payer = (int) objects[12];
                String productName = (String) objects[13];
                double weight = (double) objects[14];
                int form_delivery = (int) objects[15];
                int addon = (int) objects[16];
                double produceValue = (double) objects[17];
                String note = (String) objects[18];
                String startTime = formatDate(to.getYear(), to.getMonthOfYear(), to.getDayOfMonth(), from.getHourOfDay(), from.getMinuteOfHour());
                String endTime = formatDate(to.getYear(), to.getMonthOfYear(), to.getDayOfMonth(), to.getHourOfDay(), to.getMinuteOfHour());

                double codFee = (double) objects[19];
                double money_cash_advance = (double) objects[20];
                int type_action = (int) objects[21];
                String order_code = (String) objects[22];
                int allow_see_order = (int) objects[23];
                String shop_order_code = (String) objects[24];

                String value = APIHelper.createOrderV2(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()), StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN),
                        strSenderFullName, strSenderPhone, strSenderAddress, idSenderCity, idSenderDistrict, idSenderWard,
                        strReceiveFullName, strReceivePhone, strReceiveAddress, idReceiveCity, idReceiveDistrict, idReceiveWard,
                        payer, productName, weight, form_delivery, addon, produceValue, note, startTime, endTime, codFee, money_cash_advance,
                        files, type_action, order_code, allow_see_order, shop_order_code);
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
                    String orderCode = data.getString("order_code");
                    int payer = data.getInt("payer");
                    double money_cod = data.getDouble("money_cod");
                    double money_must_take_user = data.getDouble("money_must_take_user");
                    double money_must_shop_receive = data.getDouble("money_must_shop_receive");
                    JSONObject shipping = data.getJSONObject("shipping");
                    double shippingMoney = shipping.getDouble("money");
                    int shippingPayer = shipping.getInt("payer");
                    JSONObject sub = data.getJSONObject("sub");
                    double subMoney = sub.getDouble("money");
                    int subPayer = sub.getInt("payer");
                    JSONObject medial = data.getJSONObject("medial");
                    double medialMoney = medial.getDouble("money");
                    int medialPayer = medial.getInt("payer");
                    JSONObject protect = data.getJSONObject("protect");
                    double protectMoney = protect.getDouble("money");
                    int protectPayer = protect.getInt("payer");
                    String startTime = formatDate(from.getYear(), from.getMonthOfYear(), from.getDayOfMonth(), from.getHourOfDay(), from.getMinuteOfHour());
                    String endTime = formatDate(to.getYear(), to.getMonthOfYear(), to.getDayOfMonth(), to.getHourOfDay(), to.getMinuteOfHour());
                    CmmFunc.addFragment(getActivity(), R.id.main_container, ConfirmOrderDialogFragment.newInstance(orderCode,
                            money_cod, payer, shippingMoney, shippingPayer, medialMoney, medialPayer, protectMoney, protectPayer, subMoney, subPayer,
                            money_must_take_user, money_must_shop_receive, typeAction, getArguments().getInt("type")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                hideProgress();
            }
        }
    }
    //endregion
}

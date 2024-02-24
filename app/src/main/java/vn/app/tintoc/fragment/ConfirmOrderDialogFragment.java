package vn.app.tintoc.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.app.tintoc.R;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.Utility;

import org.json.JSONObject;

/**
 * Created by Admin on 9/1/2017.
 */

public class ConfirmOrderDialogFragment extends BaseFragment implements View.OnClickListener {
    //region Var
    ImageView ivClose;
    FrameLayout frameConfirm;
    TextView txtCODMoney, txtShippingFee, txtMediateFee, txtInsuranceFee, txtTotalGuestReceipts, txtShopIncome,
            txtChargeSide, txtSubFee;
    TextView titleCod, titleShippingFee, titleInsuranceFee, titleMediateFee, titleSubFee, titleTotalGuestReceipts, titleShopIncome,
            txtShipPayer, txtProtectPayer, txtSubPayer, txtMediatePayer, txtNotifyMediate, txtReceiverPay;
    //endregion

    //region New Instance
    public static ConfirmOrderDialogFragment newInstance(String orderCode, double money_cod, int payer, double shipping_fee,
                                                         int payerShipping, double medial_fee, int payerMedial,
                                                         double protect_fee, int payerProtect, double sub_fee, int payerSub,
                                                         double money_must_take_user, double money_must_shop_receive, int type, int typeToast) {
        Bundle args = new Bundle();
        args.putString("orderCode", orderCode);
        args.putDouble("money_cod", money_cod);
        args.putInt("payer", payer);
        args.putDouble("shipping_fee", shipping_fee);
        args.putInt("payerShipping", payerShipping);
        args.putDouble("medial_fee", medial_fee);
        args.putInt("payerMedial", payerMedial);
        args.putDouble("protect_fee", protect_fee);
        args.putInt("payerProtect", payerProtect);
        args.putDouble("sub_fee", sub_fee);
        args.putInt("payerSub", payerSub);
        args.putDouble("money_must_take_user", money_must_take_user);
        args.putDouble("money_must_shop_receive", money_must_shop_receive);
        args.putInt("type", type);
        args.putInt("typeToast", typeToast);
        ConfirmOrderDialogFragment fragment = new ConfirmOrderDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region OnCreateView
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.complete_order_dialogfragment, container, false);

        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivClose = view.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(this);
            frameConfirm = view.findViewById(R.id.frame_confirm);
            titleCod = view.findViewById(R.id.title_cod);
            txtCODMoney = view.findViewById(R.id.txt_cod_money);
            titleShippingFee = view.findViewById(R.id.title_shipping_fee);
            txtShippingFee = view.findViewById(R.id.txt_shipping_fee);
            titleMediateFee = view.findViewById(R.id.title_mediate_fee);
            txtMediateFee = view.findViewById(R.id.txt_mediate_fee);
            titleInsuranceFee = view.findViewById(R.id.title_insurance_fee);
            txtInsuranceFee = view.findViewById(R.id.txt_insurance_fee);
            titleTotalGuestReceipts = view.findViewById(R.id.title_total_guest_receipts);
            txtTotalGuestReceipts = view.findViewById(R.id.txt_total_guest_receipts);
            txtShopIncome = view.findViewById(R.id.txt_shop_income);
            txtChargeSide = view.findViewById(R.id.txt_charge_side);
            titleSubFee = view.findViewById(R.id.title_sub_fee);
            titleShopIncome = view.findViewById(R.id.title_shop_income);
            txtSubFee = view.findViewById(R.id.txt_sub_fee);
            txtShipPayer = view.findViewById(R.id.txt_ship_payer);
            txtProtectPayer = view.findViewById(R.id.txt_protect_payer);
            txtSubPayer = view.findViewById(R.id.txt_sub_payer);
            txtMediatePayer = view.findViewById(R.id.txt_mediate_payer);
            txtNotifyMediate = view.findViewById(R.id.txt_notify_mediate);
            txtReceiverPay = view.findViewById(R.id.txt_receiver_pay);
            txtCODMoney.setText(CmmFunc.formatMoney(Math.round(((getArguments().getDouble("money_cod"))))) + " VND");

            frameConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            //Hiển thị chú thích nếu có 1 khoản phí do người nhận trả
            if (getArguments().getInt("payerShipping") == 2 || getArguments().getInt("payerMedial") == 2 ||
                    getArguments().getInt("payerProtect") == 2 || getArguments().getInt("payerSub") == 2) {
                txtReceiverPay.setVisibility(View.VISIBLE);
            } else {
                txtReceiverPay.setVisibility(View.GONE);
            }

            if (getArguments().getDouble("shipping_fee") == 0) {
                titleShippingFee.setVisibility(View.GONE);
                txtShippingFee.setVisibility(View.GONE);
                txtShipPayer.setVisibility(View.GONE);
            } else {
                if (getArguments().getInt("payerShipping") == 2) {
                    txtShipPayer.setVisibility(View.VISIBLE);
                } else if (getArguments().getInt("payerShipping") == 1) {
                    txtShipPayer.setVisibility(View.GONE);
                }
                txtShippingFee.setText(CmmFunc.formatMoney(Math.round(((getArguments().getDouble("shipping_fee"))))) + " VND");
            }
            if (getArguments().getDouble("medial_fee") == 0) {
                titleMediateFee.setVisibility(View.GONE);
                txtMediateFee.setVisibility(View.GONE);
                txtMediatePayer.setVisibility(View.GONE);
                txtNotifyMediate.setVisibility(View.GONE);
            } else {
                if (getArguments().getInt("payerMedial") == 2) {
                    txtMediatePayer.setVisibility(View.VISIBLE);
                    txtNotifyMediate.setVisibility(View.VISIBLE);
                } else if (getArguments().getInt("payerMedial") == 1) {
                    txtMediatePayer.setVisibility(View.GONE);
                    txtNotifyMediate.setVisibility(View.VISIBLE);
                }
                txtMediateFee.setText(CmmFunc.formatMoney(Math.round(((getArguments().getDouble("medial_fee"))))) + " VND");
            }
            if (getArguments().getDouble("protect_fee") == 0) {
                titleInsuranceFee.setVisibility(View.GONE);
                txtInsuranceFee.setVisibility(View.GONE);
                txtProtectPayer.setVisibility(View.GONE);
            } else {
                if (getArguments().getInt("payerProtect") == 2) {
                    txtProtectPayer.setVisibility(View.VISIBLE);
                } else if (getArguments().getInt("payerProtect") == 1) {
                    txtProtectPayer.setVisibility(View.GONE);
                }
                txtInsuranceFee.setText(CmmFunc.formatMoney(Math.round(((getArguments().getDouble("protect_fee"))))) + " VND");
            }
            if (getArguments().getDouble("sub_fee") == 0) {
                titleSubFee.setVisibility(View.GONE);
                txtSubFee.setVisibility(View.GONE);
                txtSubPayer.setVisibility(View.GONE);
            } else {
                if (getArguments().getInt("payerSub") == 2) {
                    txtSubPayer.setVisibility(View.VISIBLE);
                } else if (getArguments().getInt("payerSub") == 1) {
                    txtSubPayer.setVisibility(View.GONE);
                }
                txtSubFee.setText(CmmFunc.formatMoney(Math.round(((getArguments().getDouble("sub_fee"))))) + " VND");
            }
            txtTotalGuestReceipts.setText(CmmFunc.formatMoney(Math.round(((getArguments().getDouble("money_must_take_user"))))) + " VND");

            if (getArguments().getDouble("money_must_shop_receive") == 0) {
                titleShopIncome.setVisibility(View.GONE);
                txtShopIncome.setVisibility(View.GONE);
            } else {
                double moneyShop = getArguments().getDouble("money_must_shop_receive");
                txtShopIncome.setText(CmmFunc.formatMoney(Math.round(moneyShop)) + " VND");
            }
            int iPayer = getArguments().getInt("payer");
            if (iPayer == 1) {
                txtChargeSide.setText(getString(R.string.sender));
            } else if (iPayer == 2) {
                txtChargeSide.setText(getString(R.string.receiver));
            }

            getView().findViewById(R.id.ll_received).setOnClickListener(this);
            getView().findViewById(R.id.ll_call_support).setOnClickListener(this);
            isLoad = true;
        }
    }
    //endregion

    //region Constructor
    public ConfirmOrderDialogFragment() {
    }
    //endregion

    //region OnClick
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

                            ivClose.setOnClickListener(ConfirmOrderDialogFragment.this);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.ll_received:
                CmmFunc.replaceFragment(getActivity(), R.id.main_container, HomeFragment.newInstance());
                break;
            case R.id.ll_call_support:
                Intent iHotline_1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0938264867", null));
                startActivity(iHotline_1);
                break;
        }
    }
    //endregion
}

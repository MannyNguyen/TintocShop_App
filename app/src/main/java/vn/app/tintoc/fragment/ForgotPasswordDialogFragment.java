package vn.app.tintoc.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import vn.app.tintoc.R;

/**
 * Created by IPP on 12/7/2017.
 */

public class ForgotPasswordDialogFragment extends BaseFragment implements View.OnClickListener {

    //region var
    FrameLayout frameForgotPass;
    ImageView ivClose;
    EditText edtEmail;
    Button btnChangePass;
    //endregion

    //region newInstance
    public static ForgotPasswordDialogFragment newInstance() {
        Bundle args = new Bundle();
        ForgotPasswordDialogFragment fragment = new ForgotPasswordDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    //endregion

    //region Init
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.forgot_password_dialogfragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            ivClose = view.findViewById(R.id.iv_close);
            frameForgotPass = view.findViewById(R.id.frame_forgot_pass);
            edtEmail = view.findViewById(R.id.edt_email);
            btnChangePass = view.findViewById(R.id.btn_change_pass);
            ivClose.setOnClickListener(this);
            btnChangePass.setOnClickListener(this);

            frameForgotPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Nothing
                }
            });

            edtEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String email = edtEmail.getText().toString();
                    if (email.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtEmail.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtEmail.setTypeface(customFont);
                    }
                }
            });

            isLoad = true;
        }
    }
    //endregion

    //region Override
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                break;
            case R.id.btn_change_pass:
                break;
        }
    }
    //endregion

    //region Request API

    //endregion

}

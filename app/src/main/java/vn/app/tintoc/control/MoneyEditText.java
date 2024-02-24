package vn.app.tintoc.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import vn.app.tintoc.R;
import vn.app.tintoc.config.GlobalClass;

/**
 * Created by Admin on 9/10/2017.
 */

public class MoneyEditText extends EditText {
    public MoneyEditText(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context,attrs);
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomFontTextView);
        int cf = a.getInteger(R.styleable.CustomFontTextView_font_family, 0);
        int fontName = 0;
        switch (cf)
        {
            case 1:
                fontName = R.string.segoe_light;
                break;
            case 2:
                fontName = R.string.segoe_regular;
                break;
            case 3:
                fontName = R.string.segoe_semi_bold;
                break;
            case 4:
                fontName=R.string.segoe_italic;
                break;
            case 5:
                fontName=R.string.segoe_bold;
                break;
            default:
                fontName = R.string.segoe_regular;
                break;
        }

        String customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),customFont + ".ttf");
        setTypeface(tf);
        a.recycle();
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
        setTypeface(customFont);
    }

    private String current = "";
    @Override
    public void onTextChanged(CharSequence s, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(s, start, lengthBefore, lengthAfter);
        try {
            if (!s.toString().equals(current)) {
                //Dấu chấm hoặc phẩy format giá tiền
//                String cleanString = s.toString().replaceAll("\\.", "");
                String cleanString = s.toString().replaceAll(",", "");
                for (int i = cleanString.length() - 3; i > 0; i -= 3) {
                    cleanString = new StringBuilder(cleanString).insert(i, ",").toString();
                }

                current = cleanString;
                this.setText(cleanString);
                this.setSelection(cleanString.length());
            }
        } catch (Exception e) {

        }
    }
}

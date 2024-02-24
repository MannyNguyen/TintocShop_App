package vn.app.tintoc.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import vn.app.tintoc.R;

/**
 * Created by Admin on 7/28/2017.
 */

public abstract class BaseFragment extends Fragment {

    //region Var
    public View view;
    public boolean isLoad;
    public Runnable keyBoardShow;
    public Runnable keyBoardHide;
    private boolean isKeyBoard;
    public View progress;
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Hide keyboard after change fragment

        progress = view.findViewById(R.id.progress);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        final View mRootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect measureRect = new Rect(); //you should cache this, onGlobalLayout can get called often
                mRootView.getWindowVisibleDisplayFrame(measureRect);
                // measureRect.bottom is the position above soft keypad
                int keypadHeight = mRootView.getRootView().getHeight() - (measureRect.bottom - measureRect.top);

                if (keypadHeight > 200) { // if more than 100 pixels, its probably a keyboard...
                    if (isKeyBoard) {
                        return;
                    }
                    isKeyBoard = true;
                    if (keyBoardShow != null) {
                        Log.d("KEYBOARD", "SHOW");
                        keyBoardShow.run();
                    }
                } else {
                    if (!isKeyBoard) {
                        return;
                    }
                    isKeyBoard = false;
                    if (keyBoardHide != null) {
                        Log.d("KEYBOARD", "HIDE");
                        keyBoardHide.run();
                    }
                }
            }
        });
    }

    //Ẩn/hiện Progress loading.
    public void showProgress(){
        if(progress!=null){
            progress.setVisibility(View.VISIBLE);
        }
    }
    public void hideProgress(){
        if(progress!=null){
            progress.setVisibility(View.GONE);
        }
    }
    //endregion

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

package vn.app.tintoc.config;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentActivity;

import vn.app.tintoc.utils.ConnectivityReceiver;

/**
 * Created by Admin on 7/28/2017.
 */

public class GlobalClass extends MultiDexApplication {
    private static Context mContext;
    private static FragmentActivity activity;

    private static GlobalClass mInstance;

    public static FragmentActivity getActivity() {
        return activity;
    }

    public static void setActivity(FragmentActivity activity) {
        GlobalClass.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mContext = getApplicationContext();
        mInstance = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static synchronized GlobalClass getInstance(){
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}

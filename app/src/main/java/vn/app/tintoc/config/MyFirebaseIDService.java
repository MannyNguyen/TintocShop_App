package vn.app.tintoc.config;

import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.Utility;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static vn.app.tintoc.config.GlobalClass.getContext;

public class MyFirebaseIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Lấy token từ Firebase gửi lên server
        final String token= FirebaseInstanceId.getInstance().getToken();
        //gọi API TokenFirebase trong main thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                APIHelper.tokenFireBase(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN),token, "android");
            }
        }).start();
    }

}

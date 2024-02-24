package vn.app.tintoc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmVariable;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.config.CustomDialog;
import vn.app.tintoc.config.GlobalClass;
import vn.app.tintoc.config.ICallback;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.ErrorHelper;
import vn.app.tintoc.helper.StorageHelper;

public class StartActivity extends FragmentActivity {
    public View progress;
    final int ALL_PERMISSION = 1001;

    //Không điều hướng trong Activity này, chỉ request config
    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initPermission();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GlobalClass.setActivity(this);
        progress = findViewById(R.id.progress);
        //create new storagehelper to use in fragment
        StorageHelper.init(getBaseContext());
        new GetUrl().execute();
        router();
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case CmmVariable.MY_PERMISSION_ACCESS_LOCATION:
            case CmmVariable.MY_PERMISSION_ACCESS_WIFI:
            case ALL_PERMISSION:
                router();
                break;
        }
    }


    //region Check permission for Android M, N

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case ALL_PERMISSION:
                router();
                break;
        }
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(StartActivity.this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StartActivity.this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //endregion

    //region button back
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //finishAffinity();
    }
    //endregion

    //region Methods

    //region Init Permission
    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(StartActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(StartActivity.this, "Permisson don't granted and dont show dialog again ",
                            Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }
    }
    //endregion

    //endregion

    //Ẩn/hiện Progress loading.
    public void showProgress() {
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    private void router(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Check network
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    boolean isWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                            .isConnected();
                    boolean is3g = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                            .isConnected();
                    if (!isWifi && !is3g) {
                        openNetwork();
                        return;
                    }

                    //Check location
//                    final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//                        buildAlertMessageNoGps();
//                        return;
//                    }

                    new GetUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void openNetwork(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomDialog.Dialog2Button(StartActivity.this,
                        "", getString(R.string.err_access), "Wifi", "3G",
                        new ICallback() {
                            @Override
                            public void execute() {
                                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), CmmVariable.MY_PERMISSION_ACCESS_WIFI);
                            }
                        }, new ICallback() {
                            @Override
                            public void execute() {
                                startActivityForResult(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS), CmmVariable.MY_PERMISSION_ACCESS_WIFI);
                            }
                        }
                );
            }
        });
    }

    //region Request API
    class GetUrl extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getUrl();
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
                JSONObject data = jsonObject.getJSONObject("version_config");
                StorageHelper.set(StorageHelper.URL, data.getString("url_3"));
                new ActionGetAppConfig().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ActionGetAppConfig extends Async {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.getAppConfigAPI(StorageHelper.getErrorVersion(), Config.appVersion, Config.os);
                jsonObject = new JSONObject(value);
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject error = data.getJSONObject("error");

                    int status = error.getInt("status");
                    if (status == 1) {
                        //StorageHelper.set(StorageHelper.ERROR_DESCRIPTION_EN, jsonErrorObject.getString("content"));
                        StorageHelper.set(StorageHelper.ERROR_DESCRIPTION_VN, error.getString("vn_content"));
                        StorageHelper.setErrorVersion(error.getInt("version"));
                    }
                    ErrorHelper.errorData = new JSONArray(StorageHelper.get(StorageHelper.ERROR_DESCRIPTION_VN));
                }
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
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
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

package vn.app.tintoc;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import vn.app.tintoc.adapter.LeftMenuAdapter;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.config.GlobalClass;
import vn.app.tintoc.fragment.ChooseServiceFragment;
import vn.app.tintoc.fragment.HomeFragment;
import vn.app.tintoc.fragment.ShopInfoFragment;
import vn.app.tintoc.fragment.SplashScreenFragment;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.model.LeftMenu;
import vn.app.tintoc.utils.CircleTransform;
import vn.app.tintoc.utils.Utility;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static vn.app.tintoc.config.GlobalClass.getContext;

public class MainActivity extends FragmentActivity {

    //region Var
    ImageView ivHeaderPhoto;
    TextView txtFullName;
    private RecyclerView recyclerLeftMenu;
    private LeftMenuAdapter leftMenuAdapter;
    private List<LeftMenu> leftMenuList = new ArrayList<>();
    public DrawerLayout drawerLayout;
    private ImageView ivBack;
    //endregion

    public MainActivity() {
    }

    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalClass.setActivity(this);
        init();
    }
    //endregion

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (StorageHelper.get(StorageHelper.TOKEN).equals("")) {
                    CmmFunc.addFragment(MainActivity.this, R.id.main_container, SplashScreenFragment.newInstance());
                } else {

                    CmmFunc.addFragment(MainActivity.this, R.id.main_container, HomeFragment.newInstance());
                    new GetProfileAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    //Gửi token cho server, lần tới viết vào method riêng (try/catch) tránh trường hợp không lấy được token gửi server
//                    String token = FirebaseInstanceId.getInstance().getToken();
//                    new TokenFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, token);

                    ivHeaderPhoto = findViewById(R.id.iv_header_photo);
                    txtFullName = findViewById(R.id.txt_full_name);
                    drawerLayout = findViewById(R.id.drawer_layout);
                    ivBack=findViewById(R.id.iv_back);
                    recyclerLeftMenu = findViewById(R.id.recycler_left_menu);
                    //set LinearLayout first (recyclerView)
                    leftMenuAdapter = new LeftMenuAdapter(recyclerLeftMenu, leftMenuList);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    leftMenuList.clear();
                    leftMenuList.add(new LeftMenu(LeftMenu.SHOP_INFO, getString(R.string.shop_info), "", R.drawable.ic_shopinfo));
                    leftMenuList.add(new LeftMenu(LeftMenu.INCOME, getString(R.string.debt), "", R.drawable.ic_thunhap));
                    leftMenuList.add(new LeftMenu(LeftMenu.INCENTIVE, getString(R.string.incentive), "", R.drawable.ic_uudai));
                    leftMenuList.add(new LeftMenu(LeftMenu.RATING, getString(R.string.rating), "", R.drawable.ic_danhgia));
                    leftMenuList.add(new LeftMenu(LeftMenu.SUPPORT, getString(R.string.support), "", R.drawable.ic_help));
                    leftMenuList.add(new LeftMenu(LeftMenu.LOGOUT, getString(R.string.logout), "", R.drawable.ic_logout_white));

                    ivHeaderPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CmmFunc.replaceFragment(MainActivity.this, R.id.main_container, ShopInfoFragment.newInstance());
                        }
                    });

                    ivBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CmmFunc.replaceFragment(MainActivity.this, R.id.main_container, ChooseServiceFragment.newInstance());
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerLeftMenu.setLayoutManager(layoutManager);
                            recyclerLeftMenu.setAdapter(leftMenuAdapter);
                        }
                    });

                    drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {

                        }

                        @Override
                        public void onDrawerOpened(View drawerView) {
                            try {
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {

                        }

                        @Override
                        public void onDrawerStateChanged(int newState) {

                        }
                    });

                }
            }
        }).start();
    }


    //region OnBackPress
    @Override
    public void onBackPressed() {

    }
    //endregion

    //region Request API

    //region get profile for left menu
    class GetProfileAPI extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.profileAPI(Config.COUNTRY_CODE, Utility.getDeviceID(MainActivity.this), StorageHelper.get(StorageHelper.USERNAME),
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
                    txtFullName.setText(data.getString("fullname"));
                    String urlNavHeaderBg = data.getString("avatar");
                    Glide.with(MainActivity.this).load(urlNavHeaderBg)
                            .crossFade().bitmapTransform(new CircleTransform(MainActivity.this))
                            .into(ivHeaderPhoto);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    //endregion

    //Region send token Firebase
    class TokenFirebase extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String deviceToken = (String) objects[0];
                String value = APIHelper.tokenFireBase(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        StorageHelper.get(StorageHelper.USERNAME), StorageHelper.get(StorageHelper.TOKEN), deviceToken, "android");
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
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion
}

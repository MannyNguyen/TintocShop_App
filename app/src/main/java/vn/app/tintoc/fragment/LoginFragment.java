package vn.app.tintoc.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.app.tintoc.MainActivity;
import vn.app.tintoc.R;
import vn.app.tintoc.config.Async;
import vn.app.tintoc.config.CmmFunc;
import vn.app.tintoc.config.Config;
import vn.app.tintoc.config.GlobalClass;
import vn.app.tintoc.helper.APIHelper;
import vn.app.tintoc.helper.StorageHelper;
import vn.app.tintoc.utils.ConnectivityReceiver;
import vn.app.tintoc.utils.Utility;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Executor;

public class LoginFragment extends BaseFragment implements View.OnClickListener
        , GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    //region Variables
    Button btnLogin;
    LinearLayout llRegister;
    EditText edtLoginEmail, edtLoginPassword;
    private String email, password;
    ImageView ivFacebookLogin, ivGoogleLogin;

    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> loginResult;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    //endregion

    //region Constructor
    public LoginFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Instance
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }
    //endregion

    //region Init

    //region OnCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }
    //endregion

    //region OnViewCreated
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isLoad) {
            edtLoginEmail = view.findViewById(R.id.edt_login_email);
            edtLoginPassword = view.findViewById(R.id.edt_login_password);
            btnLogin = view.findViewById(R.id.btn_login);
            ivFacebookLogin = view.findViewById(R.id.img_facebook_login);
            ivGoogleLogin = view.findViewById(R.id.img_google_login);
            llRegister = view.findViewById(R.id.ll_register);

            btnLogin.setOnClickListener(this);
            ivFacebookLogin.setOnClickListener(this);
            ivGoogleLogin.setOnClickListener(this);
            llRegister.setOnClickListener(this);

            //region setting font edittext
            edtLoginEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String text = edtLoginEmail.getText().toString();
                    if (text.equals("")) {
                        //font & size cua HINT
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_italic) + ".ttf");
                        edtLoginEmail.setTypeface(customFont);
                        return;
                    } else {
                        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.segoe_regular) + ".ttf");
                        edtLoginEmail.setTypeface(customFont);
                    }


                }
            });

            edtLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        email = edtLoginEmail.getText().toString();
                        password = edtLoginPassword.getText().toString();
                        new GetNormalLoginAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    return false;
                }
            });
            //endregion

            mAuth = FirebaseAuth.getInstance();
            printKeyHash(getActivity());
            //FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            initFacebook();
            LoginManager.getInstance().registerCallback(callbackManager, loginResult);

            isLoad = true;
        }
    }
    //endregion

    //endregion

    //region Events
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                checkConnection();
                if (edtLoginEmail.getText().toString().equals("")) {
                    edtLoginEmail.setError(getString(R.string.err_email));
                    edtLoginEmail.requestFocus();
                    return;
                }
                if (edtLoginPassword.getText().toString().equals("")) {
                    edtLoginPassword.setError(getString(R.string.err_pass));
                    edtLoginPassword.requestFocus();
                    return;
                }
                email = edtLoginEmail.getText().toString();
                password = edtLoginPassword.getText().toString();
                new GetNormalLoginAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case R.id.img_facebook_login:
                loginFacebook();
                break;
            case R.id.img_google_login:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken("86894940652-dund1ikl9rub0mljt5qsfjdr2qm1houn.apps.googleusercontent.com")
                        .requestEmail()
                        .requestId()
                        .build();
                mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                        .addConnectionCallbacks(this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                mGoogleApiClient.connect();

                mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                break;
            case R.id.ll_register:
                CmmFunc.replaceFragment(getActivity(), R.id.login_container, new RegisterFragment());
                break;
        }
    }
    //endregion

    //region Methods

    //region Check connection
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    //endregion

    //region Show snack
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (!isConnected) {
            message = "Không tìm thấy kết nối!";
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(view.findViewById(R.id.ll_login_layout), message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Check", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    });

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //endregion

    //endregion

    //region Override
    @Override
    public void onStart() {
        super.onStart();
        ivGoogleLogin.setOnClickListener(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                signOut();
            } catch (Exception e) {
                e.printStackTrace();
                Log.w(TAG, "Google sign in failed", e);
            }

        }
    }

//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//        // [START_EXCLUDE silent]
////        showProgressDialog();
//        // [END_EXCLUDE]
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
////                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//
//                        // [START_EXCLUDE]
//                        hideProgressDialog();
//                        // [END_EXCLUDE]
//                    }
//                });
//    }

    //endregion

    //region Actions

    //region Get normal login API
    class GetNormalLoginAPI extends Async {
        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject jsonObject = null;
            try {
                String value = APIHelper.loginNormalApi(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        email, password, Config.SOURCE_NORMAL);
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
                    //Save username and token into Shared preference
                    StorageHelper.set(StorageHelper.TOKEN, data.getString("token"));
                    StorageHelper.set(StorageHelper.USERNAME, data.getString("username"));

                    String token = FirebaseInstanceId.getInstance().getToken();
                    new TokenFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, token);

                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Get login Facebook API
    class GetLoginFacebookAPI extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String username = (String) objects[1];
                String accessToken = (String) objects[0];
                String value = APIHelper.loginSocialApi(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        accessToken, username, Config.SOURCE_FACEBOOK);
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
                    //Save username and token into Shared preference
                    StorageHelper.set(StorageHelper.TOKEN, data.getString("token"));
                    StorageHelper.set(StorageHelper.USERNAME, data.getString("username"));

                    String token = FirebaseInstanceId.getInstance().getToken();
                    new TokenFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, token);

                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region Get login Google API
    class GetGoogleLoginAPI extends Async {
        @Override
        protected JSONObject doInBackground(Object... objects) {
            JSONObject jsonObject = null;
            try {
                String accessToken = (String) objects[0];
                String username = (String) objects[1];
                String value = APIHelper.loginSocialApi(Config.COUNTRY_CODE, Utility.getDeviceID(getContext()),
                        accessToken, username, Config.SOURCE_GOOGLE);
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
                    //Save username and token into Shared preference
                    StorageHelper.set(StorageHelper.TOKEN, data.getString("token"));
                    StorageHelper.set(StorageHelper.USERNAME, data.getString("username"));

                    String token = FirebaseInstanceId.getInstance().getToken();
                    new TokenFirebase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, token);

                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //region send token Firebase
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    //endregion

    //region facebook login
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    //Login Facebook with permission
    public void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    //region Check login Facebook
    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
    //endregion

    //region Avatar Facebook
    public URL extractFacebookIcon(String id) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageURL = new URL("http://graph.facebook.com/" + id
                    + "/picture?type=large");
            return imageURL;
        } catch (Throwable e) {
            return null;
        }
    }
    //endregion

    //Get Facebook user info
    public void initFacebook() {
        loginResult = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                //Lấy thông tin từ facebook
                                String facebookID = object.optString("id");
                                new GetLoginFacebookAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                        AccessToken.getCurrentAccessToken().getToken(), facebookID);
                            }
                        }
                );
                Bundle parameters = new Bundle();
                parameters.putString(getString(R.string.fb_fields), getString(R.string.fb_fields_name));
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
    }

    //endregion facebook login

    //region google login
    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("onConnected", "TRUE");
        signIn();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void handleSignInResult(final GoogleSignInResult result) {
        Log.d("", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                if (acct.getId() != null && acct.getId() != "") {
                    new GetGoogleLoginAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, acct.getId(), acct.getEmail());
                }
            }

        } else {
            Log.d("", "handleSignInResult FALSE:" + result.getStatus());
        }
    }

    //endregion google login

    //region Google logout
    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
    //endregion
}

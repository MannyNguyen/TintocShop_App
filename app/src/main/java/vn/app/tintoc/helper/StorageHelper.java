package vn.app.tintoc.helper;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Admin on 7/28/2017.
 */

public class StorageHelper {
    public static final String TOKEN = "token";
    public static final String USERNAME = "username";
    public static final String ERROR_VERSION = "error_version";
    public static final String ERROR_DESCRIPTION_EN = "error_description_en";
    public static final String ERROR_DESCRIPTION_VN = "error_description_vn";
    public static final String FACEBOOK_ID = "facebook_id";
    public static final String FACEBOOK_ACCESS_TOKEN = "facebook_id";
    public static final String FACEBOOK_NAME = "facebook_name";
    public static final String GUEST_EMAIL = "guest_email";
    public static final String GUEST_TOKEN = "guest_token";
    public static final String ID_ADDRESS = "id_address";
    public static final String URL = "url";

    private static SharedPreferences preferences;

    public static void init(Context context) {
        String PREF_FILE_NAME = "TinToc_User";
        preferences = context.getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
    }

    public static void set(String key, String value) {
        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {

        }
    }

    public static String get(String key) {
        return preferences.getString(key, "");
    }

    public static void setErrorVersion(int value) {
        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(ERROR_VERSION, value);
            editor.commit();
        } catch (Exception e) {

        }
    }

    public static int getErrorVersion() {
        return preferences.getInt(ERROR_VERSION, 1);
    }
}

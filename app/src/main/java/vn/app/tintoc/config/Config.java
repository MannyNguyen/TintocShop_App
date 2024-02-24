package vn.app.tintoc.config;

import vn.app.tintoc.helper.StorageHelper;

/**
 * Created by Admin on 7/28/2017.
 */

public class Config {
    public static String appVersion = "1.0.0";
    public static String os = "ANDROID";
    public static final String COUNTRY_CODE = "84";
    public static final int SOURCE_NORMAL = 1;
    public static final int SOURCE_FACEBOOK = 2;
    public static final int SOURCE_GOOGLE = 3;

    public static final String BASE_URL = StorageHelper.get(StorageHelper.URL);
//    public static final String BASE_URL = "http://61.28.227.99:40444";
    //    public static final String BASE_URL = "http://61.28.226.106:40444";
//    public static final String BASE_URL = "http://app.tintoc.net:40444";
    public static final String DOMAIN_GET_APP_CONFIG = "/user/get_app_config";
    public static final String DOMAIN_LOGIN = "/user/login";
    public static final String DOMAIN_REGISTER = "/user/register";
    public static final String DOMAIN_GET_REGION = "/user/get_region";
    public static final String DOMAIN_GET_REGION_ACTIVE = "/user/get_region_active";
    public static final String DOMAIN_GET_PROFILE = "/user/get_profile_v2";
    public static final String DOMAIN_UPFATE_PROFILE = "/user/update_profile_v2";
    public static final String DOMAIN_GET_LIST_ORDER = "/user/get_list_order";
    public static final String DOMAIN_FILTER_ORDER = "/user/filter_order_v2";
    public static final String DOMAIN_CALCULATE_FORM_DELIVER = "/user/calculate_form_deliver";
    public static final String DOMAIN_GET_FORM_DELIVERY = "/user/get_form_delivery";
    public static final String DOMAIN_CREATE_ORDER = "/user/create_order_file";
    public static final String DOMAIN_CREATE_ORDER_2 = "/user/create_order_file_v2";
    public static final String DOMAIN_CONFIRM_CREATE_ORDER = "/user/confirm_create_order";
    public static final String DOMAIN_GET_ORDER_CONFIG = "/user/get_order_config";
    public static final String DOMAIN_GET_ORDER_BY_CODE = "/user/get_order_by_code_v2";
    public static final String DOMAIN_GUEST_GET_ORDER_BY_CODE = "/guest/get_order_by_code_v2";
    public static final String DOMAIN_RATING_ORDER = "/user/rating_order";
    public static final String DOMAIN_INCOME_PENDING = "/user/get_income";
    public static final String DOMAIN_REQUEST_SUPPORT = "/user/request_support";
    public static final String DOMAIN_SEND_RESPONSE = "/user/send_response";
    public static final String DOMAIN_SHIPPING_FEE = "/user/calculate_shipping_fee";
    public static final String DOMAIN_QUICK_REGISTER = "/guest/quick_register";
    public static final String DOMAIN_GUEST_LOGIN = "/guest/login";
    public static final String DOMAIN_UPDATE_AVATAR = "/user/update_avatar";
    public static final String DOMAIN_TOKEN_FIREBASE = "/user/update_device_token";
    public static final String DOMAIN_OFFICE_TIME = "/user/get_time_office";
    public static final String DOMAIN_GET_NOTIFY = "/user/get_notify";
    public static final String DOMAIN_ALL_REGION = "/user/get_all_region";
    public static final String DOMAIN_CHANGE_PASSWORD = "/user/change_password";
    public static final String DOMAIN_SUBMIT_ADDRESS = "/user/submit_address";
    public static final String DOMAIN_HISTORY = "/user/get_history_deliver";

    //Firebase
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}

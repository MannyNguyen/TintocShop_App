package vn.app.tintoc.helper;

import vn.app.tintoc.config.Config;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 7/28/2017.
 */

public class APIHelper {

    //get url
    public static String getUrl(){
        List<Map.Entry<String, String>> params = new ArrayList<>();
        String response = HttpHelper.get("http://center.tintoc.net/pages/version_config.php", params);
        return response;
    }

    //region Get app config
    public static String getAppConfigAPI(int errorVersion, String appVersion, String appOS) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("error_version", errorVersion + ""));
        params.add(new AbstractMap.SimpleEntry("app_version", appVersion));
        params.add(new AbstractMap.SimpleEntry("app_os", appOS));
        String configApiResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_APP_CONFIG, params);
        return configApiResponse;
    }
    //endregion

//    public static String getConfigAPI(int errorVersion, String appVersion, String appOS){
//        Map<String, String> params=new HashMap<>();
//    }


    //region Normal login
    public static String loginNormalApi(String countryCode, String deviceId, String email, String password, int source) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("email", email));
        params.add(new AbstractMap.SimpleEntry("password", password));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("source", source + ""));
        String loginNormalResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_LOGIN, params);
        return loginNormalResponse;
    }
    //endregion

    //region Social login
    public static String loginSocialApi(String countryCode, String deviceId, String accessToken, String userName,
                                        int source) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("access_token", accessToken));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("source", source + ""));
        String loginSocialResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_LOGIN, params);
        return loginSocialResponse;
    }
    //endregion

    //region Register
    public static String registerAPI(String countryCode, String deviceId, String email, String password,
                                     String confirmPassword, String fullName, String phone, String address,
                                     int locateCity, String locateCityName, int locateDistrict, String locateDistrictName,
                                     int locateWard, String locateWardName, int source) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("email", email));
        params.add(new AbstractMap.SimpleEntry("password", password));
        params.add(new AbstractMap.SimpleEntry("confirm_password", confirmPassword));
        params.add(new AbstractMap.SimpleEntry("fullname", fullName));
        params.add(new AbstractMap.SimpleEntry("phone", phone));
        params.add(new AbstractMap.SimpleEntry("address", address));
        params.add(new AbstractMap.SimpleEntry("locate_city", locateCity + ""));
        params.add(new AbstractMap.SimpleEntry("locate_city_name", locateCityName));
        params.add(new AbstractMap.SimpleEntry("locate_district", locateDistrict + ""));
        params.add(new AbstractMap.SimpleEntry("locate_district_name", locateDistrictName));
        params.add(new AbstractMap.SimpleEntry("locate_ward", locateWard + ""));
        params.add(new AbstractMap.SimpleEntry("locate_ward_name", locateWardName));
        params.add(new AbstractMap.SimpleEntry("source", source + ""));
        String registerResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_REGISTER, params);
        return registerResponse;
    }
    //endregion

    //region Get region
    public static String getRegionAPI(int typeRegion, int cityId, int districtId) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("type_region", typeRegion + ""));
        if (cityId > 0) {
            params.add(new AbstractMap.SimpleEntry("cityid", cityId + ""));
        }
        if (districtId > 0) {
            params.add(new AbstractMap.SimpleEntry("districtid", districtId + ""));
        }

        String regionResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_REGION, params);
        return regionResponse;
    }
    //endregion

    //region Get region active
    public static String getRegionActiveAPI(int typeRegion, int cityId, int districtId) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("type_region", typeRegion + ""));
        if (cityId > 0) {
            params.add(new AbstractMap.SimpleEntry("cityid", cityId + ""));
        }
        if (districtId > 0) {
            params.add(new AbstractMap.SimpleEntry("districtid", districtId + ""));
        }

        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_REGION_ACTIVE, params);
        return response;
    }
    //endregion

    //region Get profile
    public static String profileAPI(String countryCode, String deviceId, String userName, String token) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        String profileResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_PROFILE, params);
        return profileResponse;
    }
    //endregion

    //region Update profile
    public static String updateProfile(String countryCode, String deviceId, String userName, String token,
                                       String fullName, String email, String account_bank, String name_bank, String ctk_bank) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("fullname", fullName));
        params.add(new AbstractMap.SimpleEntry("email", email));
        params.add(new AbstractMap.SimpleEntry("account_bank", account_bank));
        params.add(new AbstractMap.SimpleEntry("name_bank", name_bank));
        params.add(new AbstractMap.SimpleEntry("ctk_bank", ctk_bank));
        String updateProfileResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_UPFATE_PROFILE, params);
        return updateProfileResponse;
    }
    //endregion

    //region Filer order
    public static String filterOrder(String countryCode, String deviceId, String userName, String token,
                                     String keysearch, String time_from, String time_to) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("keysearch", keysearch));
        params.add(new AbstractMap.SimpleEntry("time_from", time_from));
        params.add(new AbstractMap.SimpleEntry("time_to", time_to));
        String filterOrderResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_FILTER_ORDER, params);
        return filterOrderResponse;
    }
    //endregion

    //region Config order
    public static String configOrder(String countryCode, String deviceId, String userName, String token) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        String configOrderResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_ORDER_CONFIG, params);
        return configOrderResponse;
    }
    //endregion

    //region Get list order
    public static String getListOrder(String countryCode, String deviceId, String userName, String token) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        String getListOrderResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_LIST_ORDER, params);
        return getListOrderResponse;
    }
    //endregion

    //region Calculate form deliver first
    public static String calculateFormDeliverFirst(String countryCode, String deviceId, String userName, String token,
                                                   int address_delivery_city, int address_delivery_district) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("address_delivery_city", address_delivery_city + ""));
        params.add(new AbstractMap.SimpleEntry("address_delivery_district", address_delivery_district + ""));
        String getcalculateFormDeliverFirstResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CALCULATE_FORM_DELIVER, params);
        return getcalculateFormDeliverFirstResponse;
    }

    public static String calculateFormDeliver(String countryCode, String deviceId, String userName, String token,
                                              int address_delivery_city, int address_delivery_district) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("address_delivery_city", address_delivery_city + ""));
        params.add(new AbstractMap.SimpleEntry("address_delivery_district", address_delivery_district + ""));
        String getcalculateFormDeliverFirstResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CALCULATE_FORM_DELIVER, params);
        return getcalculateFormDeliverFirstResponse;
    }

    public static String calculateFormDeliver(String countryCode, String deviceId, String userName, String token,
                                              int address_delivery_city, int address_delivery_district,
                                              String time_delivery_from, String time_delivery_to) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("address_delivery_city", address_delivery_city + ""));
        params.add(new AbstractMap.SimpleEntry("address_delivery_district", address_delivery_district + ""));
        params.add(new AbstractMap.SimpleEntry("time_delivery_from", time_delivery_from));
        params.add(new AbstractMap.SimpleEntry("time_delivery_to", time_delivery_to));
        String getCalculateFormDeliverSecondResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CALCULATE_FORM_DELIVER, params);
        return getCalculateFormDeliverSecondResponse;
    }

    public static String getFormDelivery(String countryCode, String deviceID, String userName, String token, int addressDeliveryCity, int addressDeliveryDistrict) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceID));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("address_delivery_city", addressDeliveryCity + ""));
        params.add(new AbstractMap.SimpleEntry("address_delivery_district", addressDeliveryDistrict + ""));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_FORM_DELIVERY, params);
        return response;
    }
    //endregion

    //region Calculate dorm delivery second
    public static String calculateFormDeliverSecond(String countryCode, String deviceId, String userName, String token,
                                                    int address_delivery_city, int address_delivery_district,
                                                    String time_delivery_from, String time_delivery_to) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("address_delivery_city", address_delivery_city + ""));
        params.add(new AbstractMap.SimpleEntry("address_delivery_district", address_delivery_district + ""));
        params.add(new AbstractMap.SimpleEntry("time_delivery_from", time_delivery_from));
        params.add(new AbstractMap.SimpleEntry("time_delivery_to", time_delivery_to));
        String getCalculateFormDeliverSecondResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CALCULATE_FORM_DELIVER, params);
        return getCalculateFormDeliverSecondResponse;
    }
    //endregion

    //region Create order
    public static String createOrder(String countryCode,
                                     String deviceId, String username,
                                     String sender_fullname,
                                     String sender_phone,
                                     String sender_address,
                                     int sender_city,
                                     int sender_district,
                                     int sender_ward,

                                     String receiver_fullname,
                                     String receiver_phone,
                                     String receiver_address,
                                     int receiver_city,
                                     int receiver_district,
                                     int receiver_ward,

                                     int payer,
                                     String product_type,
                                     int product_weight,
                                     int form_delivery,
                                     int addon,
                                     double product_value,
                                     String note,
                                     String time_delivery_from,
                                     String time_delivery_to,
                                     double money_cod,
                                     double money_cash_advance,
                                     List<File> files,
                                     int type_action,
                                     String order_code,
                                     int insurance_service,
                                     String note_change_service,
                                     String note_form_deliver_car,
                                     int ems_service,
                                     int allow_see_order) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", username));
        params.add(new AbstractMap.SimpleEntry("sender_fullname", sender_fullname));
        params.add(new AbstractMap.SimpleEntry("sender_phone", sender_phone));
        params.add(new AbstractMap.SimpleEntry("sender_address", sender_address));
        params.add(new AbstractMap.SimpleEntry("sender_city", sender_city + ""));
        params.add(new AbstractMap.SimpleEntry("sender_district", sender_district + ""));
        params.add(new AbstractMap.SimpleEntry("sender_ward", sender_ward + ""));
        params.add(new AbstractMap.SimpleEntry("receiver_fullname", receiver_fullname));
        params.add(new AbstractMap.SimpleEntry("receiver_phone", receiver_phone));
        params.add(new AbstractMap.SimpleEntry("receiver_address", receiver_address));
        params.add(new AbstractMap.SimpleEntry("receiver_city", receiver_city + ""));
        params.add(new AbstractMap.SimpleEntry("receiver_district", receiver_district + ""));
        params.add(new AbstractMap.SimpleEntry("receiver_ward", receiver_ward + ""));
        params.add(new AbstractMap.SimpleEntry("form_delivery", form_delivery + ""));
        params.add(new AbstractMap.SimpleEntry("payer", payer + ""));
        params.add(new AbstractMap.SimpleEntry("product_type", product_type));
        params.add(new AbstractMap.SimpleEntry("product_weight", product_weight + ""));
        params.add(new AbstractMap.SimpleEntry("addon", addon + ""));
        params.add(new AbstractMap.SimpleEntry("product_value", product_value + ""));
        params.add(new AbstractMap.SimpleEntry("note", note));
        params.add(new AbstractMap.SimpleEntry("time_delivery_from", time_delivery_from));
        params.add(new AbstractMap.SimpleEntry("time_delivery_to", time_delivery_to));
        params.add(new AbstractMap.SimpleEntry("money_cod", money_cod + ""));
        params.add(new AbstractMap.SimpleEntry("money_cash_advance", money_cash_advance + ""));
        params.add(new AbstractMap.SimpleEntry("type_action", type_action + ""));
        params.add(new AbstractMap.SimpleEntry("order_code", order_code));
        params.add(new AbstractMap.SimpleEntry("insurance_service", insurance_service + ""));
        params.add(new AbstractMap.SimpleEntry("note_change_service", note_change_service));
        params.add(new AbstractMap.SimpleEntry("note_form_deliver_car", note_form_deliver_car));
        params.add(new AbstractMap.SimpleEntry("ems_service", ems_service + ""));
        params.add(new AbstractMap.SimpleEntry("allow_see_order", allow_see_order + ""));
        String createOrderResponse = HttpHelper.postFile(Config.BASE_URL + Config.DOMAIN_CREATE_ORDER, params, files);
        return createOrderResponse;
    }
    //endregion

    //region Create order v2
    public static String createOrderV2(String country_code,
                                       String deviceid,
                                       String username,
                                       String token,
                                       String sender_fullname,
                                       String sender_phone,
                                       String sender_address,
                                       int sender_city,
                                       int sender_district,
                                       int sender_ward,
                                       String receiver_fullname,
                                       String receiver_phone,
                                       String receiver_address,
                                       int receiver_city,
                                       int receiver_district,
                                       int receiver_ward,
                                       int payer,
                                       String product_type,
                                       double product_weight,
                                       int form_delivery,
                                       int addon,
                                       double product_value,
                                       String note,
                                       String time_delivery_from,
                                       String time_delivery_to,
                                       double money_cod,
                                       double money_cash_advance,
                                       List<File> files,
                                       int type_action,
                                       String orderCode,
                                       int allow_see_order,
                                       String shop_order_code) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", country_code));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceid));
        params.add(new AbstractMap.SimpleEntry("username", username));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("sender_fullname", sender_fullname));
        params.add(new AbstractMap.SimpleEntry("sender_phone", sender_phone));
        params.add(new AbstractMap.SimpleEntry("sender_address", sender_address));
        params.add(new AbstractMap.SimpleEntry("sender_city", sender_city + ""));
        params.add(new AbstractMap.SimpleEntry("sender_district", sender_district + ""));
        params.add(new AbstractMap.SimpleEntry("sender_ward", sender_ward + ""));
        params.add(new AbstractMap.SimpleEntry("receiver_fullname", receiver_fullname));
        params.add(new AbstractMap.SimpleEntry("receiver_phone", receiver_phone));
        params.add(new AbstractMap.SimpleEntry("receiver_address", receiver_address));
        params.add(new AbstractMap.SimpleEntry("receiver_city", receiver_city + ""));
        params.add(new AbstractMap.SimpleEntry("receiver_district", receiver_district + ""));
        params.add(new AbstractMap.SimpleEntry("receiver_ward", receiver_ward + ""));
        params.add(new AbstractMap.SimpleEntry("payer", payer + ""));
        params.add(new AbstractMap.SimpleEntry("product_type", product_type));
        params.add(new AbstractMap.SimpleEntry("product_weight", product_weight + ""));
        params.add(new AbstractMap.SimpleEntry("form_delivery", form_delivery + ""));
        params.add(new AbstractMap.SimpleEntry("addon", addon + ""));
        params.add(new AbstractMap.SimpleEntry("product_value", product_value + ""));
        params.add(new AbstractMap.SimpleEntry("note", note));
        params.add(new AbstractMap.SimpleEntry("time_delivery_from", time_delivery_from));
        params.add(new AbstractMap.SimpleEntry("time_delivery_to", time_delivery_to));
        params.add(new AbstractMap.SimpleEntry("money_cod", money_cod + ""));
        params.add(new AbstractMap.SimpleEntry("money_cash_advance", money_cash_advance + ""));
        params.add(new AbstractMap.SimpleEntry("type_action", type_action + ""));
        params.add(new AbstractMap.SimpleEntry("order_code", orderCode));
        params.add(new AbstractMap.SimpleEntry("allow_see_order", allow_see_order + ""));
        params.add(new AbstractMap.SimpleEntry("shop_order_code", shop_order_code));
        String response = HttpHelper.postFile(Config.BASE_URL + Config.DOMAIN_CREATE_ORDER_2, params, files);
        return response;
    }
    //endregion

    //region Confirm create order
    public static String confirmCreateOrder(String countryCode, String deviceId, String userName, String token, String order_code, int type) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", order_code));
        params.add(new AbstractMap.SimpleEntry("type", type + ""));
        String createConfirmOrderResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CONFIRM_CREATE_ORDER, params);
        return createConfirmOrderResponse;
    }
    //endregion

    //region Get order by code
    public static String getOrderByCode(String countryCode, String deviceId, String userName,
                                        String token, String orderCode) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", orderCode));
        String getOrderByCodeResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_ORDER_BY_CODE, params);
        return getOrderByCodeResponse;
    }
    //endregion

    //region Get guest order by code
    public static String getGuestOrderByCode(String countryCode, String deviceId, String userName,
                                             String token, String orderCode) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", orderCode));
        String getGuestOrderByCodeResponse = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GUEST_GET_ORDER_BY_CODE, params);
        return getGuestOrderByCodeResponse;
    }
    //endregion

    //region Rating shipper
    public static String ratingShipper(String countryCode, String deviceId, String userName, String token,
                                       String orderCode, int star_shipper, int star_time_deliver, String reason) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", orderCode));
        params.add(new AbstractMap.SimpleEntry("star_shipper", star_shipper + ""));
        params.add(new AbstractMap.SimpleEntry("star_time_deliver", star_time_deliver + ""));
        params.add(new AbstractMap.SimpleEntry("reason", reason));
        String ratingShipperResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_RATING_ORDER, params);
        return ratingShipperResponse;
    }
    //endregion

    //region Pending income
    public static String getDebt(String countryCode, String deviceId, String userName, String token,
                                 String timeFrom, String timeTo, int type) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("timefrom", timeFrom));
        params.add(new AbstractMap.SimpleEntry("timeto", timeTo));
        params.add(new AbstractMap.SimpleEntry("type", type + ""));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_INCOME_PENDING, params);
        return response;
    }
    //endregion

    //region Send request support
    public static String sendRequestSupport(String countryCode, String deviceId, String userName, String token,
                                            String content) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("content", content));
        String sendRequestSupportResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_REQUEST_SUPPORT, params);
        return sendRequestSupportResponse;
    }
    //endregion

    //region Send response
    public static String sendResponse(String countryCode, String deviceId, String userName, String token,
                                      String content) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("content", content));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_SEND_RESPONSE, params);
        return response;
    }
    //endregion

    //region Shippng fee
    public static String shippingFee(String countryCode, String deviceId, String userName, String token,
                                     int city_receiver, int district_receiver, int productWeight, String time_delivery_from,
                                     String time_delivery_to, double product_value) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("city_receiver", city_receiver + ""));
        params.add(new AbstractMap.SimpleEntry("district_receiver", district_receiver + ""));
        params.add(new AbstractMap.SimpleEntry("productWeight", productWeight + ""));
        params.add(new AbstractMap.SimpleEntry("time_delivery_from", time_delivery_from));
        params.add(new AbstractMap.SimpleEntry("time_delivery_to", time_delivery_to));
        params.add(new AbstractMap.SimpleEntry("product_value", product_value + ""));
        String shippingFeeResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_SHIPPING_FEE, params);
        return shippingFeeResponse;
    }
    //endregion

    //region Quick register
    public static String quickRegister(String countryCode, String deviceId, String userName, String password) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("password", password));
        String quickRegisterResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_QUICK_REGISTER, params);
        return quickRegisterResponse;
    }
    //endregion

    //region Guest login
    public static String guestLogin(String countryCode, String deviceId, String userName, String password) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("password", password));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_GUEST_LOGIN, params);
        return response;
    }
    //endregion

    //region Update avatar
    public static String updateAvatar(String countryCode, String deviceId, String userName, String token, File avatar) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("avatar", avatar));
        String updateAvatarResponse = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_UPDATE_AVATAR, params);
        return updateAvatarResponse;
    }
    //endregion

    //region Token Firebase
    public static String tokenFireBase(String countryCode, String deviceId, String userName, String token,
                                       String device_token, String os) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("device_token", device_token));
        params.add(new AbstractMap.SimpleEntry("os", os));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_TOKEN_FIREBASE, params);
        return response;
    }
    //endregion

    //region Office time
    public static String officeTime(String countryCode, String deviceId, String userName, String token,
                                    int address_delivery_city, int address_delivery_district, String date_delivery_from,
                                    String date_delivery_to) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("address_delivery_city", address_delivery_city + ""));
        params.add(new AbstractMap.SimpleEntry("address_delivery_district", address_delivery_district + ""));
        params.add(new AbstractMap.SimpleEntry("date_delivery_from", date_delivery_from));
        params.add(new AbstractMap.SimpleEntry("date_delivery_to", date_delivery_to));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_OFFICE_TIME, params);
        return response;
    }
    //endregion

    //region Notify
    public static String getNotify(String countryCode, String deviceId, String userName, String token) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_GET_NOTIFY, params);
        return response;
    }
    //endregion

    //region Change password
    public static String getChangePassword(String countryCode, String deviceId, String userName,
                                           String token, String oldPassword, String newPassword) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("old_password", oldPassword));
        params.add(new AbstractMap.SimpleEntry("new_password", newPassword));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_CHANGE_PASSWORD, params);
        return response;
    }
    //endregion

    //region submit address
    public static String getSubmitAddress(String countryCode, String deviceId, String userName,
                                          String token, String phone, String name, String address, int city_id, int district_id,
                                          int ward_id, int action, int id_address) {
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("phone", phone));
        params.add(new AbstractMap.SimpleEntry("name", name));
        params.add(new AbstractMap.SimpleEntry("address", address));
        params.add(new AbstractMap.SimpleEntry("city_id", city_id + ""));
        params.add(new AbstractMap.SimpleEntry("district_id", district_id + ""));
        params.add(new AbstractMap.SimpleEntry("ward_id", ward_id + ""));
        params.add(new AbstractMap.SimpleEntry("action", action + ""));
        params.add(new AbstractMap.SimpleEntry("id_address", id_address + ""));
        String response = HttpHelper.post(Config.BASE_URL + Config.DOMAIN_SUBMIT_ADDRESS, params);
        return response;
    }
    //endregion

    //region Get history
    public static String getHistory(String countryCode, String deviceId, String userName,
                                    String token,String order_code){
        List<Map.Entry<String, String>> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry("country_code", countryCode));
        params.add(new AbstractMap.SimpleEntry("deviceid", deviceId));
        params.add(new AbstractMap.SimpleEntry("username", userName));
        params.add(new AbstractMap.SimpleEntry("token", token));
        params.add(new AbstractMap.SimpleEntry("order_code", order_code));
        String response = HttpHelper.get(Config.BASE_URL + Config.DOMAIN_HISTORY, params);
        return response;
    }
    //endregion
}

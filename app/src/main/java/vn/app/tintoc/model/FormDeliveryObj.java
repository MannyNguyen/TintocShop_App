package vn.app.tintoc.model;

import java.util.List;

/**
 * Created by Admin on 7/31/2017.
 */

public class FormDeliveryObj {
    private int form_deliver_id;
    private String form_deliver_name;
    private boolean active_time;
    private String time_from_default;
    private String time_to_default;
    private String date_from_default;
    private String date_to_default;
    private double shipping_fee;
    private boolean active_ems;
    private boolean is_form_deliver_car;
    private boolean allow_see_order;
    private boolean is_viettel_post;
    private boolean is_return_product;

    public FormDeliveryObj() {
    }

    public FormDeliveryObj(int form_deliver_id, String form_deliver_name, boolean active_time, String time_from_default,
                           String time_to_default, String date_from_default, String date_to_default, double shipping_fee) {
        this.form_deliver_id = form_deliver_id;
        this.form_deliver_name = form_deliver_name;
        this.active_time = active_time;
        this.time_from_default = time_from_default;
        this.time_to_default = time_to_default;
        this.date_from_default = date_from_default;
        this.date_to_default = date_to_default;
        this.shipping_fee = shipping_fee;
    }

    public static FormDeliveryObj getValueByKey(int id, List<FormDeliveryObj>list){
        for (FormDeliveryObj obj : list) {
            if (obj.getForm_deliver_id() == id) {
                return obj;
            }
        }
        return null;
    }

    public int getForm_deliver_id() {
        return form_deliver_id;
    }

    public void setForm_deliver_id(int form_deliver_id) {
        this.form_deliver_id = form_deliver_id;
    }

    public String getForm_deliver_name() {
        return form_deliver_name;
    }

    public void setForm_deliver_name(String form_deliver_name) {
        this.form_deliver_name = form_deliver_name;
    }

    public boolean isActive_time() {
        return active_time;
    }

    public void setActive_time(boolean active_time) {
        this.active_time = active_time;
    }

    public String getTime_from_default() {
        return time_from_default;
    }

    public void setTime_from_default(String time_from_default) {
        this.time_from_default = time_from_default;
    }

    public String getTime_to_default() {
        return time_to_default;
    }

    public void setTime_to_default(String time_to_default) {
        this.time_to_default = time_to_default;
    }

    public String getDate_from_default() {
        return date_from_default;
    }

    public void setDate_from_default(String date_from_default) {
        this.date_from_default = date_from_default;
    }

    public String getDate_to_default() {
        return date_to_default;
    }

    public void setDate_to_default(String date_to_default) {
        this.date_to_default = date_to_default;
    }

    public double getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(double shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public boolean isActive_ems() {
        return active_ems;
    }

    public void setActive_ems(boolean active_ems) {
        this.active_ems = active_ems;
    }

    public boolean isAllow_see_order() {
        return allow_see_order;
    }

    public void setAllow_see_order(boolean allow_see_order) {
        this.allow_see_order = allow_see_order;
    }

    public boolean is_viettel_post() {
        return is_viettel_post;
    }

    public void setIs_viettel_post(boolean is_viettel_post) {
        this.is_viettel_post = is_viettel_post;
    }

    public boolean isIs_form_deliver_car() {
        return is_form_deliver_car;
    }

    public void setIs_form_deliver_car(boolean is_form_deliver_car) {
        this.is_form_deliver_car = is_form_deliver_car;
    }

    public boolean isIs_viettel_post() {
        return is_viettel_post;
    }

    public boolean isIs_return_product() {
        return is_return_product;
    }

    public void setIs_return_product(boolean is_return_product) {
        this.is_return_product = is_return_product;
    }
}

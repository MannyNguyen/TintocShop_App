package vn.app.tintoc.model;

/**
 * Created by Admin on 8/7/2017.
 */

public class OrderResultObj {
    private String order_code;
    private String date_delivery;
    private String status;
    private int status_id;
    private String create_date;
    private String receiver_fullname;
    private String address;
    private double total;
    private String time_update_payment;
    private String shop_order_code;
    private String time_update_status;
    private int payment_status;

    public String getReceiver_fullname() {
        return receiver_fullname;
    }

    public void setReceiver_fullname(String receiver_fullname) {
        this.receiver_fullname = receiver_fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public OrderResultObj() {
    }

    public OrderResultObj(String order_code, String date_delivery, String status, int status_id, String create_date) {
        this.order_code = order_code;
        this.date_delivery = date_delivery;
        this.status = status;
        this.status_id = status_id;
        this.create_date = create_date;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getDate_delivery() {
        return date_delivery;
    }

    public void setDate_delivery(String date_delivery) {
        this.date_delivery = date_delivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getTime_update_payment() {
        return time_update_payment;
    }

    public void setTime_update_payment(String time_update_payment) {
        this.time_update_payment = time_update_payment;
    }

    public String getShop_order_code() {
        return shop_order_code;
    }

    public void setShop_order_code(String shop_order_code) {
        this.shop_order_code = shop_order_code;
    }

    public String getTime_update_status() {
        return time_update_status;
    }

    public void setTime_update_status(String time_update_status) {
        this.time_update_status = time_update_status;
    }

    public int getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }
}

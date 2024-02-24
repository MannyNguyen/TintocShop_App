package vn.app.tintoc.model;

/**
 * Created by Admin on 9/27/2017.
 */

public class IncomeObj {
    private String stt;
    private String order_code;
    private String address;
    private String total_cod;
    private String total_shop;
    private String total_fee;
    private String payment_type;
    private String income;
    private String method;
    private String fullname;

    public IncomeObj() {
    }

    public IncomeObj(String stt, String order_code, String address, String total_shop, String total_fee, String income, String method) {
        this.stt = stt;
        this.order_code = order_code;
        this.address = address;
        this.total_shop = total_shop;
        this.total_fee = total_fee;
        this.income = income;
        this.method = method;
    }

    public String getTotal_cod() {
        return total_cod;
    }

    public void setTotal_cod(String total_cod) {
        this.total_cod = total_cod;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal_shop() {
        return total_shop;
    }

    public void setTotal_shop(String total_shop) {
        this.total_shop = total_shop;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}

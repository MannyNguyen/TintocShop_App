package vn.app.tintoc.model;

import java.io.Serializable;

/**
 * Created by Admin on 9/4/2017.
 */

public class ConfirmOrderObj implements Serializable {
    private String receiver_fullname;
    private String receiver_phone;
    private String address_receive;
    private int address_receive_city;
    private int address_receive_district;
    private String sender_fullname;
    private String sender_phone;
    private String address_delivery;
    private int address_delivery_city;
    private int address_delivery_district;
    private int payer;
    private int product_type;
    private int product_weight;
    private int form_delivery;
    private String note;
    private String time_delivery_from;
    private String time_delivery_to;
    private double shipping_fee;
    private double money_code;

    public ConfirmOrderObj() {
    }

    public ConfirmOrderObj(String receiver_fullname, String receiver_phone, String address_receive, int address_receive_city,
                           int address_receive_district, String sender_fullname, String sender_phone, String address_delivery,
                           int address_delivery_city, int address_delivery_district, int payer, int product_type,
                           int product_weight, int form_delivery, String note, String time_delivery_from,
                           String time_delivery_to, double shipping_fee, double money_code) {
        this.receiver_fullname = receiver_fullname;
        this.receiver_phone = receiver_phone;
        this.address_receive = address_receive;
        this.address_receive_city = address_receive_city;
        this.address_receive_district = address_receive_district;
        this.sender_fullname = sender_fullname;
        this.sender_phone = sender_phone;
        this.address_delivery = address_delivery;
        this.address_delivery_city = address_delivery_city;
        this.address_delivery_district = address_delivery_district;
        this.payer = payer;
        this.product_type = product_type;
        this.product_weight = product_weight;
        this.form_delivery = form_delivery;
        this.note = note;
        this.time_delivery_from = time_delivery_from;
        this.time_delivery_to = time_delivery_to;
        this.shipping_fee = shipping_fee;
        this.money_code = money_code;
    }

    public String getReceiver_fullname() {
        return receiver_fullname;
    }

    public void setReceiver_fullname(String receiver_fullname) {
        this.receiver_fullname = receiver_fullname;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getAddress_receive() {
        return address_receive;
    }

    public void setAddress_receive(String address_receive) {
        this.address_receive = address_receive;
    }

    public int getAddress_receive_city() {
        return address_receive_city;
    }

    public void setAddress_receive_city(int address_receive_city) {
        this.address_receive_city = address_receive_city;
    }

    public int getAddress_receive_district() {
        return address_receive_district;
    }

    public void setAddress_receive_district(int address_receive_district) {
        this.address_receive_district = address_receive_district;
    }

    public String getSender_fullname() {
        return sender_fullname;
    }

    public void setSender_fullname(String sender_fullname) {
        this.sender_fullname = sender_fullname;
    }

    public String getSender_phone() {
        return sender_phone;
    }

    public void setSender_phone(String sender_phone) {
        this.sender_phone = sender_phone;
    }

    public String getAddress_delivery() {
        return address_delivery;
    }

    public void setAddress_delivery(String address_delivery) {
        this.address_delivery = address_delivery;
    }

    public int getAddress_delivery_city() {
        return address_delivery_city;
    }

    public void setAddress_delivery_city(int address_delivery_city) {
        this.address_delivery_city = address_delivery_city;
    }

    public int getAddress_delivery_district() {
        return address_delivery_district;
    }

    public void setAddress_delivery_district(int address_delivery_district) {
        this.address_delivery_district = address_delivery_district;
    }

    public int getPayer() {
        return payer;
    }

    public void setPayer(int payer) {
        this.payer = payer;
    }

    public int getProduct_type() {
        return product_type;
    }

    public void setProduct_type(int product_type) {
        this.product_type = product_type;
    }

    public int getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(int product_weight) {
        this.product_weight = product_weight;
    }

    public int getForm_delivery() {
        return form_delivery;
    }

    public void setForm_delivery(int form_delivery) {
        this.form_delivery = form_delivery;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime_delivery_from() {
        return time_delivery_from;
    }

    public void setTime_delivery_from(String time_delivery_from) {
        this.time_delivery_from = time_delivery_from;
    }

    public String getTime_delivery_to() {
        return time_delivery_to;
    }

    public void setTime_delivery_to(String time_delivery_to) {
        this.time_delivery_to = time_delivery_to;
    }

    public double getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(double shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public double getMoney_code() {
        return money_code;
    }

    public void setMoney_code(double money_code) {
        this.money_code = money_code;
    }
}

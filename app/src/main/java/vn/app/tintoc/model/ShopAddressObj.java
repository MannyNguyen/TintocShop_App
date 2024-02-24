package vn.app.tintoc.model;

/**
 * Created by IPP on 1/19/2018.
 */

public class ShopAddressObj {
    private int city_id;
    private String city_name;
    private String phone;
    private String district_name;
    private String address;
    private String name;
    private String ward_name;
    private int district_id;
    private int ward_id;
    private int id_address;

    public ShopAddressObj() {
    }

    public ShopAddressObj(int city_id, String city_name, String phone, String district_name, String address, String name, String ward_name, int district_id, int ward_id, int id_address) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.phone = phone;
        this.district_name = district_name;
        this.address = address;
        this.name = name;
        this.ward_name = ward_name;
        this.district_id = district_id;
        this.ward_id = ward_id;
        this.id_address = id_address;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public int getWard_id() {
        return ward_id;
    }

    public void setWard_id(int ward_id) {
        this.ward_id = ward_id;
    }

    public int getId_address() {
        return id_address;
    }

    public void setId_address(int id_address) {
        this.id_address = id_address;
    }
}

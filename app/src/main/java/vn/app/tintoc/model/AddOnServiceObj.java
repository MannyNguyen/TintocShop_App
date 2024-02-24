package vn.app.tintoc.model;

import java.util.List;

/**
 * Created by Admin on 9/19/2017.
 */

public class AddOnServiceObj {
    private int id;
    private String name;
    private int is_show_product_value;
    private boolean isActive = true;

    public static AddOnServiceObj getValueByKey(int id, List<AddOnServiceObj> list) {
        for (AddOnServiceObj obj : list) {
            if (obj.getId() == id) {
                return obj;
            }
        }
        return null;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_show_product_value() {
        return is_show_product_value;
    }

    public void setIs_show_product_value(int is_show_product_value) {
        this.is_show_product_value = is_show_product_value;
    }
}

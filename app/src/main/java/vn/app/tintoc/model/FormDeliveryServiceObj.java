package vn.app.tintoc.model;

import java.util.List;

/**
 * Created by Admin on 9/19/2017.
 */

public class FormDeliveryServiceObj {
    private int id;
    private String name;
    private double medial_fee ;

    public static FormDeliveryServiceObj getValueByKey(int id, List<FormDeliveryServiceObj> list){
        for (FormDeliveryServiceObj obj : list) {
            if (obj.getId() == id) {
                return obj;
            }
        }
        return null;
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

    public double getMedial_fee() {
        return medial_fee;
    }

    public void setMedial_fee(double medial_fee) {
        this.medial_fee = medial_fee;
    }
}

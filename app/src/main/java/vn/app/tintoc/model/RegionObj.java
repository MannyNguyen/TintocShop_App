package vn.app.tintoc.model;

import java.util.List;

/**
 * Created by Admin on 7/30/2017.
 */

public class RegionObj {
    private int id;
    private String name;
    private int code;

    public RegionObj(int id, String name, int code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public RegionObj() {

    }

    public static RegionObj getValueByKey(int id, List<RegionObj> list) {
        for (RegionObj obj : list) {
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

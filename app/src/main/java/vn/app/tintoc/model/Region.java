package vn.app.tintoc.model;

import java.util.List;

/**
 * Created by Admin on 10/17/2017.
 */

public class Region {
    private int id;
    private int code;
    private String name;
    private int type;
    private int parent;
    List<Region>childs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public List<Region> getChilds() {
        return childs;
    }

    public void setChilds(List<Region> childs) {
        this.childs = childs;
    }
}

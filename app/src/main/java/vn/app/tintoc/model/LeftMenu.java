package vn.app.tintoc.model;

import java.util.List;

/**
 * Created by Admin on 7/31/2017.
 */

public class LeftMenu {
    public static final int SHOP_INFO = 0;
    public static final int INCOME = 1;
    public static final int INCENTIVE = 2;
    public static final int RATING = 3;
    public static final int SUPPORT = 4;
    public static final int LOGOUT = 5;

    private int type;
    private String textMenu, numNotify;
    private int ivIconMenu;
    private boolean showNotify;

    public LeftMenu() {
    }

    public LeftMenu(int type, String textMenu, String numNotify, int ivIconMenu) {
        this.type = type;
        this.textMenu = textMenu;
        this.numNotify = numNotify;
        this.ivIconMenu = ivIconMenu;
    }

    public static LeftMenu getByType(int type, List<LeftMenu> items) {
        for (LeftMenu item : items) {
            if (type == item.getType()) {
                return item;
            }
        }
        return null;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTextMenu() {
        return textMenu;
    }

    public void setTextMenu(String textMenu) {
        this.textMenu = textMenu;
    }

    public String getNumNotify() {
        return numNotify;
    }

    public void setNumNotify(String numNotify) {
        this.numNotify = numNotify;
    }

    public int getIvIconMenu() {
        return ivIconMenu;
    }

    public void setIvIconMenu(int ivIconMenu) {
        this.ivIconMenu = ivIconMenu;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

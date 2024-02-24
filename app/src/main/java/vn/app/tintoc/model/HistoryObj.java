package vn.app.tintoc.model;

/**
 * Created by IPP on 1/24/2018.
 */

public class HistoryObj {
    private int id;
    private String note;
    private String time_confirm;

    public HistoryObj() {
    }

    public HistoryObj(int id, String note, String time_confirm) {
        this.id = id;
        this.note = note;
        this.time_confirm = time_confirm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime_confirm() {
        return time_confirm;
    }

    public void setTime_confirm(String time_confirm) {
        this.time_confirm = time_confirm;
    }
}

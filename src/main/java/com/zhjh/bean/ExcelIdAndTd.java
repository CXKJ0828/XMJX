package com.zhjh.bean;

/**
 * Created by Administrator on 2018/7/18.
 */
public class ExcelIdAndTd {
    public int id;
    public String td;

    public ExcelIdAndTd(int id, String td) {
        this.id = id;
        this.td = td;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTd() {
        return td;
    }

    public void setTd(String td) {
        this.td = td;
    }
}

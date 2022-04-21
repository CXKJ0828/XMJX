package com.zhjh.bean;

/**
 * Created by Administrator on 2018/7/9.
 */
public class ExcelSheetBean {
    public int number;
    public String name;

    public ExcelSheetBean() {
    }

    public ExcelSheetBean(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

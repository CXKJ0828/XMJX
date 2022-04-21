package com.zhjh.bean;

/**
 * Created by Administrator on 2018/7/5.
 */
public class ChangeResultBean {
    public String name;
    public String url;
    public boolean result;
    public int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChangeResultBean(String name, String url, boolean result) {
        this.name = name;
        this.url = url;
        this.result = result;
    }

    public ChangeResultBean() {
    }
}

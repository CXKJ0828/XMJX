package com.zhjh.bean;

/**
 * Created by Administrator on 2018/7/5.
 */
public class FileBean {
    public String name;
    public String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileBean(String name, String path) {
        this.name = name;
        this.path = path;
    }
}

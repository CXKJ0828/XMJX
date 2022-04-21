package com.zhjh.bean;

/**
 * Created by Administrator on 2018/7/20.
 */
public class GeTuiMessage {
    public String title;//通知栏标题
    public String content;//通知栏内容
    public String message;//透传内容

    public GeTuiMessage(String title, String content, String message) {
        this.title = title;
        this.content = content;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

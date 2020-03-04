package com.patelheggere.farmconnect.model.notify;

public class PushNotificationModel {
    private String title;
    private String msg;
    private int type;

    public PushNotificationModel(String title, String msg, int type) {
        this.title = title;
        this.msg = msg;
        this.type = type;
    }

    public PushNotificationModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

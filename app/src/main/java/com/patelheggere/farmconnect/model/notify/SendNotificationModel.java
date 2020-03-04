package com.patelheggere.farmconnect.model.notify;

public class SendNotificationModel {
    private String collapse_key;
    private Data data;
    private Notification notification;
    private String[]registration_ids;

    public SendNotificationModel() {
    }

    public SendNotificationModel(String collapse_key, Data data, Notification notification, String[] registration_ids) {
        this.collapse_key = collapse_key;
        this.data = data;
        this.notification = notification;
        this.registration_ids = registration_ids;
    }

    public String getCollapse_key() {
        return collapse_key;
    }

    public void setCollapse_key(String collapse_key) {
        this.collapse_key = collapse_key;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String[] getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(String[] registration_ids) {
        this.registration_ids = registration_ids;
    }

}

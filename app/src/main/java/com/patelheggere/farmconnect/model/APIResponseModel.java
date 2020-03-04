package com.patelheggere.farmconnect.model;

public class APIResponseModel {
    private boolean status;
    private String msg;
    private String name;
    private String phone;
    private String id;
    private String email;
    private String imageURL;
    public APIResponseModel() {
    }

    public APIResponseModel(boolean status, String msg, String name, String phone, String id, String email, String imageURL) {
        this.status = status;
        this.msg = msg;
        this.name = name;
        this.phone = phone;
        this.id = id;
        this.email = email;
        this.imageURL = imageURL;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}

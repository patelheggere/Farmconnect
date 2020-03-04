package com.patelheggere.farmconnect.model;

public class LoginModel {
    private String phone;
    private String pwd;

    public LoginModel() {
    }

    public LoginModel(String phone, String pwd) {
        this.phone = phone;
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

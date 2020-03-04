package com.patelheggere.farmconnect.model;

public class UserDetails {
    private String name;
    private String phone;
    private String userid;
    private String email;
    private String fcm_token;
    private String password;
    private String avatar;
    private String dist;
    private String taluk;
    private String hobli;
    private String gp;
    private String village;
    private String type;
    private String state;


    public UserDetails() {
    }

    public UserDetails(String name, String phone, String userid, String email, String fcm_token, String password, String avatar, String dist, String taluk, String hobli, String gp, String village) {
        this.name = name;
        this.phone = phone;
        this.userid = userid;
        this.email = email;
        this.fcm_token = fcm_token;
        this.password = password;
        this.avatar = avatar;
        this.dist = dist;
        this.taluk = taluk;
        this.hobli = hobli;
        this.gp = gp;
        this.village = village;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getHobli() {
        return hobli;
    }

    public void setHobli(String hobli) {
        this.hobli = hobli;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return phone;
    }

    public void setMobile(String mobile) {
        this.phone = mobile;
    }

    public String getInterest() {
        return userid;
    }

    public void setInterest(String interest) {
        this.userid = interest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getPwd() {
        return password;
    }

    public void setPwd(String pwd) {
        this.password = pwd;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

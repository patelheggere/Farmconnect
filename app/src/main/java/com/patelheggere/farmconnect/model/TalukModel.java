package com.patelheggere.farmconnect.model;

public class TalukModel {
    private String tlk_id;
    private String name;
    private String name_kn;
    private String district;
    private String dist_id;
    private String created_on;
    private String updated_on;

    public TalukModel() {
    }

    public TalukModel(String tlk_id, String name, String name_kn, String district, String dist_id, String created_on, String updated_on) {
        this.tlk_id = tlk_id;
        this.name = name;
        this.name_kn = name_kn;
        this.district = district;
        this.dist_id = dist_id;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    public String getTlk_id() {
        return tlk_id;
    }

    public void setTlk_id(String tlk_id) {
        this.tlk_id = tlk_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_kn() {
        return name_kn;
    }

    public void setName_kn(String name_kn) {
        this.name_kn = name_kn;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDist_id() {
        return dist_id;
    }

    public void setDist_id(String dist_id) {
        this.dist_id = dist_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }
}

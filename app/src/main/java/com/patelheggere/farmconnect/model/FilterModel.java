package com.patelheggere.farmconnect.model;

public class FilterModel {
    private String cropName;
    private String state;
    private String district;
    private String taluk;
    private String village;

    public FilterModel() {
    }

    public FilterModel(String cropName, String state, String district, String taluk, String village) {
        this.cropName = cropName;
        this.state = state;
        this.district = district;
        this.taluk = taluk;
        this.village = village;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }
}

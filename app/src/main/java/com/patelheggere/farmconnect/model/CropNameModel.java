package com.patelheggere.farmconnect.model;

public class CropNameModel {
    private String name;
    private String nameKn;
    private String cropIcon;

    public CropNameModel() {
    }

    public CropNameModel(String name, String nameKn, String cropIcon) {
        this.name = name;
        this.nameKn = nameKn;
        this.cropIcon = cropIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKn() {
        return nameKn;
    }

    public void setNameKn(String nameKn) {
        this.nameKn = nameKn;
    }

    public String getCropIcon() {
        return cropIcon;
    }

    public void setCropIcon(String cropIcon) {
        this.cropIcon = cropIcon;
    }
}

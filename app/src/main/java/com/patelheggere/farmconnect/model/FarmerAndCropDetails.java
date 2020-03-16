package com.patelheggere.farmconnect.model;

public class FarmerAndCropDetails {
    private String cropName;
    private int bidderPrice;
    private int farmerPrice;
    private String cropLocation;
    private String cropState;
    private String cropDistrict;
    private String cropTaluk;
    private String cropVillageId;
    private String farmersName;
    private String farmerPhone;
    private String email;
    private String farmerDist;
    private String farmerTaluk;
    private String farmerVillage;

    public FarmerAndCropDetails() {
    }

    public FarmerAndCropDetails(String cropName, int bidderPrice, int farmerPrice, String cropLocation, String cropState, String cropDistrict, String cropTaluk, String cropVillageId, String farmersName, String farmerPhone, String email, String farmerDist, String farmerTaluk, String farmerVillage) {
        this.cropName = cropName;
        this.bidderPrice = bidderPrice;
        this.farmerPrice = farmerPrice;
        this.cropLocation = cropLocation;
        this.cropState = cropState;
        this.cropDistrict = cropDistrict;
        this.cropTaluk = cropTaluk;
        this.cropVillageId = cropVillageId;
        this.farmersName = farmersName;
        this.farmerPhone = farmerPhone;
        this.email = email;
        this.farmerDist = farmerDist;
        this.farmerTaluk = farmerTaluk;
        this.farmerVillage = farmerVillage;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public int getBidderPrice() {
        return bidderPrice;
    }

    public void setBidderPrice(int bidderPrice) {
        this.bidderPrice = bidderPrice;
    }

    public int getFarmerPrice() {
        return farmerPrice;
    }

    public void setFarmerPrice(int farmerPrice) {
        this.farmerPrice = farmerPrice;
    }

    public String getCropLocation() {
        return cropLocation;
    }

    public void setCropLocation(String cropLocation) {
        this.cropLocation = cropLocation;
    }

    public String getCropState() {
        return cropState;
    }

    public void setCropState(String cropState) {
        this.cropState = cropState;
    }

    public String getCropDistrict() {
        return cropDistrict;
    }

    public void setCropDistrict(String cropDistrict) {
        this.cropDistrict = cropDistrict;
    }

    public String getCropTaluk() {
        return cropTaluk;
    }

    public void setCropTaluk(String cropTaluk) {
        this.cropTaluk = cropTaluk;
    }

    public String getCropVillageId() {
        return cropVillageId;
    }

    public void setCropVillageId(String cropVillageId) {
        this.cropVillageId = cropVillageId;
    }

    public String getFarmersName() {
        return farmersName;
    }

    public void setFarmersName(String farmersName) {
        this.farmersName = farmersName;
    }

    public String getFarmerPhone() {
        return farmerPhone;
    }

    public void setFarmerPhone(String farmerPhone) {
        this.farmerPhone = farmerPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFarmerDist() {
        return farmerDist;
    }

    public void setFarmerDist(String farmerDist) {
        this.farmerDist = farmerDist;
    }

    public String getFarmerTaluk() {
        return farmerTaluk;
    }

    public void setFarmerTaluk(String farmerTaluk) {
        this.farmerTaluk = farmerTaluk;
    }

    public String getFarmerVillage() {
        return farmerVillage;
    }

    public void setFarmerVillage(String farmerVillage) {
        this.farmerVillage = farmerVillage;
    }
}

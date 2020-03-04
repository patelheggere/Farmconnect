package com.patelheggere.farmconnect.model;

public class PostBidModel {
    private String cropName;
    private long uploadedTime;
    private int NoOfBids;
    private String qty;
    private String cropImage;
    private long harvestingTime;
    private String status;
    private String bidderPrice;
    private String farmerPrice;
    private int isBidSuccess;
    private String userid;
    private String cropLocation;
    private String sl_no;
    private long endTime;
    private String bidderID;

    public PostBidModel() {
    }

    public PostBidModel(String bidderId, String cropName, long uploadedTime, int noOfBids, String qty, String cropImage, long harvestingTime, String status, String bidderPrice, String farmerPrice, int isBidSuccess, String userid, String cropLocation, String sl_no, long endTime) {
        this.cropName = cropName;
        this.uploadedTime = uploadedTime;
        NoOfBids = noOfBids;
        this.qty = qty;
        this.cropImage = cropImage;
        this.harvestingTime = harvestingTime;
        this.status = status;
        this.bidderPrice = bidderPrice;
        this.farmerPrice = farmerPrice;
        this.isBidSuccess = isBidSuccess;
        this.userid = userid;
        this.cropLocation = cropLocation;
        this.sl_no = sl_no;
        this.endTime = endTime;
        this.bidderID = bidderId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public long getUploadedTime() {
        return uploadedTime;
    }

    public void setUploadedTime(long uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    public int getNoOfBids() {
        return NoOfBids;
    }

    public void setNoOfBids(int noOfBids) {
        NoOfBids = noOfBids;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }

    public long getHarvestingTime() {
        return harvestingTime;
    }

    public void setHarvestingTime(long harvestingTime) {
        this.harvestingTime = harvestingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBidderPrice() {
        return bidderPrice;
    }

    public void setBidderPrice(String bidderPrice) {
        this.bidderPrice = bidderPrice;
    }

    public String getFarmerPrice() {
        return farmerPrice;
    }

    public void setFarmerPrice(String farmerPrice) {
        this.farmerPrice = farmerPrice;
    }

    public int getIsBidSuccess() {
        return isBidSuccess;
    }

    public void setIsBidSuccess(int isBidSuccess) {
        this.isBidSuccess = isBidSuccess;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCropLocation() {
        return cropLocation;
    }

    public void setCropLocation(String cropLocation) {
        this.cropLocation = cropLocation;
    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getBidderID() {
        return bidderID;
    }

    public void setBidderID(String bidderID) {
        this.bidderID = bidderID;
    }
}

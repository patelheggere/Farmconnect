package com.patelheggere.farmconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FarmerCropModel implements Parcelable {
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
    private String state;
    private String stateId;
    private String district;
    private String districtId;
    private String taluk;
    private String talukId;
    private String village;
    private String villageId;
    private String cropImageIcon;
    private long cropListId;


    public FarmerCropModel() {
    }

    public FarmerCropModel(long cropListId, String cropImageIcon, String cropName, long uploadedTime, int noOfBids, String qty, String cropImage, long harvestingTime, String status, String bidderPrice, String farmerPrice, int isBidSuccess, String userid, String cropLocation, String sl_no, long endTime, String state, String stateId, String district, String districtId, String taluk, String talukId, String village, String villageId) {
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
        this.state = state;
        this.stateId = stateId;
        this.district = district;
        this.districtId = districtId;
        this.taluk = taluk;
        this.talukId = talukId;
        this.village = village;
        this.villageId = villageId;
        this.cropImageIcon = cropImageIcon;
    }

    protected FarmerCropModel(Parcel in) {
        cropName = in.readString();
        uploadedTime = in.readLong();
        NoOfBids = in.readInt();
        qty = in.readString();
        cropImage = in.readString();
        harvestingTime = in.readLong();
        status = in.readString();
        bidderPrice = in.readString();
        farmerPrice = in.readString();
        isBidSuccess = in.readInt();
        userid = in.readString();
        cropLocation = in.readString();
        sl_no = in.readString();
        endTime = in.readLong();
        cropImageIcon = in.readString();
        cropListId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cropName);
        dest.writeLong(uploadedTime);
        dest.writeInt(NoOfBids);
        dest.writeString(qty);
        dest.writeString(cropImage);
        dest.writeLong(harvestingTime);
        dest.writeString(status);
        dest.writeString(bidderPrice);
        dest.writeString(farmerPrice);
        dest.writeInt(isBidSuccess);
        dest.writeString(userid);
        dest.writeString(cropLocation);
        dest.writeString(sl_no);
        dest.writeLong(endTime);
        dest.writeString(cropImageIcon);
        dest.writeLong(cropListId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FarmerCropModel> CREATOR = new Creator<FarmerCropModel>() {
        @Override
        public FarmerCropModel createFromParcel(Parcel in) {
            return new FarmerCropModel(in);
        }

        @Override
        public FarmerCropModel[] newArray(int size) {
            return new FarmerCropModel[size];
        }
    };

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

    public int isBidSuccess() {
        return isBidSuccess;
    }

    public void setBidSuccess(int bidSuccess) {
        isBidSuccess = bidSuccess;
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

    public int getIsBidSuccess() {
        return isBidSuccess;
    }

    public void setIsBidSuccess(int isBidSuccess) {
        this.isBidSuccess = isBidSuccess;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public static Creator<FarmerCropModel> getCREATOR() {
        return CREATOR;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getTalukId() {
        return talukId;
    }

    public void setTalukId(String talukId) {
        this.talukId = talukId;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getCropImageIcon() {
        return cropImageIcon;
    }

    public void setCropImageIcon(String cropImageIcon) {
        this.cropImageIcon = cropImageIcon;
    }

    public long getCropListId() {
        return cropListId;
    }

    public void setCropListId(long cropListId) {
        this.cropListId = cropListId;
    }
}

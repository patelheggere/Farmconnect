package com.patelheggere.farmconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FarmerBidNotificationModel implements Parcelable {
    private String cropName;
    private long uploadedTime;
    private String bidder_id;
    private int biddingPrice;
    private int isBidAccepted;
    private int sl_no;
    private int bidding_id;
    private String farmer_id;
    private int crop_id;
    private int isPaymentMade;
    private int notificationId;


    public FarmerBidNotificationModel() {

    }


    public FarmerBidNotificationModel(int notificationId, int isPaymentMade, String cropName, long uploadedTime, String bidder_id, int biddingPrice, int isBidAccepted, int sl_no, int bidding_id, String farmer_id, int crop_id) {
        this.cropName = cropName;
        this.uploadedTime = uploadedTime;
        this.bidder_id = bidder_id;
        this.biddingPrice = biddingPrice;
        this.isBidAccepted = isBidAccepted;
        this.sl_no = sl_no;
        this.bidding_id = bidding_id;
        this.farmer_id = farmer_id;
        this.crop_id = crop_id;
        this.isPaymentMade = isPaymentMade;
        this.notificationId = notificationId;
    }

    protected FarmerBidNotificationModel(Parcel in) {
        cropName = in.readString();
        uploadedTime = in.readLong();
        bidder_id = in.readString();
        biddingPrice = in.readInt();
        isBidAccepted = in.readInt();
        sl_no = in.readInt();
        isPaymentMade = in.readInt();
        notificationId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cropName);
        dest.writeLong(uploadedTime);
        dest.writeString(bidder_id);
        dest.writeInt(biddingPrice);
        dest.writeInt(isBidAccepted);
        dest.writeInt(sl_no);
        dest.writeInt(isPaymentMade);
        dest.writeInt(notificationId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FarmerBidNotificationModel> CREATOR = new Creator<FarmerBidNotificationModel>() {
        @Override
        public FarmerBidNotificationModel createFromParcel(Parcel in) {
            return new FarmerBidNotificationModel(in);
        }

        @Override
        public FarmerBidNotificationModel[] newArray(int size) {
            return new FarmerBidNotificationModel[size];
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

    public String getBidder_id() {
        return bidder_id;
    }

    public void setBidder_id(String bidder_id) {
        this.bidder_id = bidder_id;
    }

    public int getBiddingPrice() {
        return biddingPrice;
    }

    public void setBiddingPrice(int biddingPrice) {
        this.biddingPrice = biddingPrice;
    }

    public int getIsBidAccepted() {
        return isBidAccepted;
    }

    public void setIsBidAccepted(int isBidAccepted) {
        this.isBidAccepted = isBidAccepted;
    }

    public int getSl_no() {
        return sl_no;
    }

    public void setSl_no(int sl_no) {
        this.sl_no = sl_no;
    }

    public static Creator<FarmerBidNotificationModel> getCREATOR() {
        return CREATOR;
    }

    public int getBidding_id() {
        return bidding_id;
    }

    public void setBidding_id(int bidding_id) {
        this.bidding_id = bidding_id;
    }

    public String getFarmer_id() {
        return farmer_id;
    }

    public void setFarmer_id(String farmer_id) {
        this.farmer_id = farmer_id;
    }

    public int getCrop_id() {
        return crop_id;
    }

    public void setCrop_id(int crop_id) {
        this.crop_id = crop_id;
    }

    public int getIsPaymentMade() {
        return isPaymentMade;
    }

    public void setIsPaymentMade(int isPaymentMade) {
        this.isPaymentMade = isPaymentMade;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}

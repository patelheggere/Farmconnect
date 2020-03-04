package com.patelheggere.farmconnect.activity.merchantmain.ui.liveauction.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LiveAuctionModel implements Parcelable {
    private String title;
    private String expiryDateTime;
    private String uploadedDateTime;
    private String cropImageURL;
    private String bidAmount;
    private String currentBidPrice;
    private String details;
    private String location;
    private String minBidPrice;
    private boolean isBidByuser;

    public LiveAuctionModel() {
    }

    public LiveAuctionModel(boolean isBidByuser, String title, String expiryDateTime, String uploadedDateTime, String cropImageURL, String bidAmount, String currentBidPrice, String details, String location, String minBidPrice) {
        this.title = title;
        this.expiryDateTime = expiryDateTime;
        this.uploadedDateTime = uploadedDateTime;
        this.cropImageURL = cropImageURL;
        this.bidAmount = bidAmount;
        this.currentBidPrice = currentBidPrice;
        this.details = details;
        this.location = location;
        this.minBidPrice = minBidPrice;
        this.isBidByuser = isBidByuser;
    }

    protected LiveAuctionModel(Parcel in) {
        title = in.readString();
        expiryDateTime = in.readString();
        uploadedDateTime = in.readString();
        cropImageURL = in.readString();
        bidAmount = in.readString();
        currentBidPrice = in.readString();
        details = in.readString();
        location = in.readString();
        minBidPrice = in.readString();
        isBidByuser = in.readByte() != 0;
    }

    public static final Creator<LiveAuctionModel> CREATOR = new Creator<LiveAuctionModel>() {
        @Override
        public LiveAuctionModel createFromParcel(Parcel in) {
            return new LiveAuctionModel(in);
        }

        @Override
        public LiveAuctionModel[] newArray(int size) {
            return new LiveAuctionModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(String expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public String getUploadedDateTime() {
        return uploadedDateTime;
    }

    public void setUploadedDateTime(String uploadedDateTime) {
        this.uploadedDateTime = uploadedDateTime;
    }

    public String getCropImageURL() {
        return cropImageURL;
    }

    public void setCropImageURL(String cropImageURL) {
        this.cropImageURL = cropImageURL;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getCurrentBidPrice() {
        return currentBidPrice;
    }

    public void setCurrentBidPrice(String currentBidPrice) {
        this.currentBidPrice = currentBidPrice;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMinBidPrice() {
        return minBidPrice;
    }

    public void setMinBidPrice(String minBidPrice) {
        this.minBidPrice = minBidPrice;
    }

    public boolean isBidByuser() {
        return isBidByuser;
    }

    public void setBidByuser(boolean bidByuser) {
        isBidByuser = bidByuser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(expiryDateTime);
        parcel.writeString(uploadedDateTime);
        parcel.writeString(cropImageURL);
        parcel.writeString(bidAmount);
        parcel.writeString(currentBidPrice);
        parcel.writeString(details);
        parcel.writeString(location);
        parcel.writeString(minBidPrice);
        parcel.writeByte((byte) (isBidByuser ? 1 : 0));
    }
}

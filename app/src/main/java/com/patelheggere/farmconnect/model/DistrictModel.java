package com.patelheggere.farmconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DistrictModel implements Parcelable {
    String dist_id;
    String dist_name;
    String dist_name_kn;
    String created_on;
    String updated_on;

    public DistrictModel() {
    }

    public DistrictModel(String dist_id, String dist_name, String dist_name_kn, String created_on, String updated_on) {
        this.dist_id = dist_id;
        this.dist_name = dist_name;
        this.dist_name_kn = dist_name_kn;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    protected DistrictModel(Parcel in) {
        dist_id = in.readString();
        dist_name = in.readString();
        dist_name_kn = in.readString();
        created_on = in.readString();
        updated_on = in.readString();
    }

    public static final Creator<DistrictModel> CREATOR = new Creator<DistrictModel>() {
        @Override
        public DistrictModel createFromParcel(Parcel in) {
            return new DistrictModel(in);
        }

        @Override
        public DistrictModel[] newArray(int size) {
            return new DistrictModel[size];
        }
    };

    public String getDist_id() {
        return dist_id;
    }

    public void setDist_id(String dist_id) {
        this.dist_id = dist_id;
    }

    public String getDist_name() {
        return dist_name;
    }

    public void setDist_name(String dist_name) {
        this.dist_name = dist_name;
    }

    public String getDist_name_kn() {
        return dist_name_kn;
    }

    public void setDist_name_kn(String dist_name_kn) {
        this.dist_name_kn = dist_name_kn;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dist_id);
        parcel.writeString(dist_name);
        parcel.writeString(dist_name_kn);
        parcel.writeString(created_on);
        parcel.writeString(updated_on);
    }
}

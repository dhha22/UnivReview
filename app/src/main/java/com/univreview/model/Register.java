package com.univreview.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by DavidHa on 2017. 1. 15..
 */
public class Register implements Parcelable {
    public String userType;
    public String userId;
    public String accessToken;
    public String nickName;
    public String profileUrl;
    public Long universityId;
    public Long departmentId;
    public Long majorId;
    public Uri profileUri;

    public Register(String userType, String userId, String accessToken, String nickName, String profileUrl) {
        this.userType = userType;
        this.userId = userId;
        this.accessToken = accessToken;
        this.nickName = nickName;
        this.profileUrl = profileUrl;
    }




    @Override
    public String toString() {
        return "Register{" +
                "userType='" + userType + '\'' +
                ", userId='" + userId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", universityId=" + universityId +
                ", departmentId=" + departmentId +
                ", majorId=" + majorId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userType);
        dest.writeString(this.userId);
        dest.writeString(this.accessToken);
        dest.writeString(this.nickName);
        dest.writeString(this.profileUrl);
        dest.writeValue(this.universityId);
        dest.writeValue(this.departmentId);
        dest.writeValue(this.majorId);
        dest.writeParcelable(this.profileUri, flags);
    }

    protected Register(Parcel in) {
        this.userType = in.readString();
        this.userId = in.readString();
        this.accessToken = in.readString();
        this.nickName = in.readString();
        this.profileUrl = in.readString();
        this.universityId = (Long) in.readValue(Long.class.getClassLoader());
        this.departmentId = (Long) in.readValue(Long.class.getClassLoader());
        this.majorId = (Long) in.readValue(Long.class.getClassLoader());
        this.profileUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator<Register> CREATOR = new Parcelable.Creator<Register>() {
        @Override
        public Register createFromParcel(Parcel source) {
            return new Register(source);
        }

        @Override
        public Register[] newArray(int size) {
            return new Register[size];
        }
    };
}

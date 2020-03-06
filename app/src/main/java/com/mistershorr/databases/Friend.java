package com.mistershorr.databases;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable {

    private int clumsiness;
    private int gymFrequency;
    private boolean Awesome;
    private double moneyOwed;
    private String name;
    private int trustworthiness;
    // backendless specific fields

    private String objectId;
    private String ownerId;

    // MUST HAVE A DEFAULT, NO PARAM CONSTRUCTOR (FOR BACKENDLESS TO WORK)
    public Friend() {

    }

    // MUST HAVE GETTERS & SETTERS
    public int getClumsiness() {
        return clumsiness;
    }

    public void setClumsiness(int clumsiness) {
        this.clumsiness = clumsiness;
    }

    public int getGymFrequency() {
        return gymFrequency;
    }

    public void setGymFrequency(int gymFrequency) {
        this.gymFrequency = gymFrequency;
    }

    public boolean isAwesome() {
        return Awesome;
    }

    public void setAwesome(boolean awesome) {
        Awesome = awesome;
    }

    public double getMoneyOwed() {
        return moneyOwed;
    }

    public void setMoneyOwed(double moneyOwed) {
        this.moneyOwed = moneyOwed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTrustworthiness() {
        return trustworthiness;
    }

    public void setTrustworthiness(int trustworthiness) {
        this.trustworthiness = trustworthiness;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.clumsiness);
        dest.writeInt(this.gymFrequency);
        dest.writeByte(this.Awesome ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.moneyOwed);
        dest.writeString(this.name);
        dest.writeInt(this.trustworthiness);
        dest.writeString(this.objectId);
        dest.writeString(this.ownerId);
    }

    protected Friend(Parcel in) {
        this.clumsiness = in.readInt();
        this.gymFrequency = in.readInt();
        this.Awesome = in.readByte() != 0;
        this.moneyOwed = in.readDouble();
        this.name = in.readString();
        this.trustworthiness = in.readInt();
        this.objectId = in.readString();
        this.ownerId = in.readString();
    }

    public static final Creator<Friend> CREATOR = new Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}


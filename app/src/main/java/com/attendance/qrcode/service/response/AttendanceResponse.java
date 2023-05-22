package com.attendance.qrcode.service.response;

import com.google.gson.annotations.SerializedName;

public class AttendanceResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("att_date")
    private String logDate;
    @SerializedName("att_type")
    private int type;
    @SerializedName("att_clock_type")
    private int clockType;
    @SerializedName("att_latitude")
    private double latitude;
    @SerializedName("att_longtitude")
    private double longtitude;
    @SerializedName("user_id")
    private long userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getClockType() {
        return clockType;
    }

    public void setClockType(int clockType) {
        this.clockType = clockType;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

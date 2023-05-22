package com.attendance.qrcode.service.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceListResponse {

    @SerializedName("data")
    List<AttendanceResponse> data;

    public List<AttendanceResponse> getData() {
        return data;
    }

    public void setData(List<AttendanceResponse> data) {
        this.data = data;
    }
}

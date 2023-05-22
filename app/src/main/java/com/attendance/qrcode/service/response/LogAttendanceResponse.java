package com.attendance.qrcode.service.response;

import com.google.gson.annotations.SerializedName;

public class LogAttendanceResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("statusCode")
    private int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

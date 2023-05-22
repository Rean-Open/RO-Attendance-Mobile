package com.attendance.qrcode.service.response;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("data")
    private UserResponse data;
    @SerializedName("token")
    private TokenResponse token;
    @SerializedName("message")
    private String message;
    @SerializedName("statusCode")
    private int statusCode;

    public UserResponse getData() {
        return data;
    }

    public void setData(UserResponse data) {
        this.data = data;
    }

    public TokenResponse getToken() {
        return token;
    }

    public void setToken(TokenResponse token) {
        this.token = token;
    }

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

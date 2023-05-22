package com.attendance.qrcode.service;

import com.attendance.qrcode.service.response.AuthResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @POST("/api/mobile/auth")
    Call<AuthResponse> auth(@Query("email") String email, @Query("password") String password);

}

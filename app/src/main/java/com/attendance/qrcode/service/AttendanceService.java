package com.attendance.qrcode.service;

import com.attendance.qrcode.service.response.AttendanceListResponse;
import com.attendance.qrcode.service.response.AuthResponse;
import com.attendance.qrcode.service.response.LogAttendanceResponse;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AttendanceService {

    @GET("/api/mobile/list-attendant")
    Call<AttendanceListResponse> list();

    @FormUrlEncoded
    @POST("/api/mobile/log-attendant")
    Call<LogAttendanceResponse> logAttendance(@Query("att_type") int type,
                                              @Query("att_clock_type") int clockType,
                                              @Query("att_latitude") double latitude,
                                              @Query("att_longtitude") double longtitude);
}

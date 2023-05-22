package com.attendance.qrcode.service;

import android.content.Context;

import com.attendance.qrcode.AuthHelper;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static APIClient instance;

    private UserService userService;
    private AttendanceService attendanceService;
    private Context context;

    private APIClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        List<String> pathSegments = request.url().pathSegments();
                        if (pathSegments.get(pathSegments.size()-1).equals("auth")) {
                            return chain.proceed(request);
                        }

                        String accessToken = AuthHelper.getAccessToken(context);
                        Request newRequest = request.newBuilder()
                                .header("Authorization", "Bearer " + accessToken).build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.1.6:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userService = retrofit.create(UserService.class);
        attendanceService = retrofit.create(AttendanceService.class);
    }

    public static synchronized APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    public UserService getUserService(Context context) {
        this.context = context;
        return userService;
    }

    public AttendanceService getAttendanceService(Context context) {
        this.context = context;
        return attendanceService;
    }
}

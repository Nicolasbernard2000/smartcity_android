package com.example.smartcity_app.repositories.web;

import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repositories.web.dto.ReportDto;
import com.example.smartcity_app.repositories.web.dto.ReportTypeDto;
import com.example.smartcity_app.repositories.web.dto.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WalloniaFixedWebService {
    @GET("v1/report")
    Call<List<ReportDto>> getReports();

    @GET("v1/report/foruser/{userId}")
    Call<List<ReportDto>> getReportsWithUserId(@Path("userId") int userId);

    @POST("v1/report")
    Call<Object> postReport(@Body ReportDto reportDto);

    @GET("v1/user/{id}")
    Call<UserDto> getUser(@Path("id") int userId);

    @POST("v1/user")
    Call<UserDto> postUser(@Body UserDto userDto);

    @GET("v1/reportType")
    Call<List<ReportTypeDto>> getReportTypes();
}

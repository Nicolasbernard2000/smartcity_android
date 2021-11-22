package com.example.smartcity_app.repositories.web;

import com.example.smartcity_app.model.User;
import com.example.smartcity_app.repositories.web.dto.ReportDto;
import com.example.smartcity_app.repositories.web.dto.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WalloniaFixedWebService {
    @GET("v1/report")
    Call<List<ReportDto>> getReports();

    @GET("v1/user/{id}")
    Call<UserDto> getUser(@Path("id") int userId);
}

package com.example.smartcity_app.repositories.web;

import com.example.smartcity_app.repositories.web.dto.ReportDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WalloniaFixedWebService {
    @GET("v1/report")
    Call<List<ReportDto>> getReports();
}

package com.example.smartcity_app.repository.web;

import com.example.smartcity_app.repository.web.dto.EventDto;
import com.example.smartcity_app.repository.web.dto.LoginDto;
import com.example.smartcity_app.repository.web.dto.ReportDto;
import com.example.smartcity_app.repository.web.dto.ReportTypeDto;
import com.example.smartcity_app.repository.web.dto.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WalloniaFixedWebService {
    @GET("v1/report")
    Call<List<ReportDto>> getReports();

    @GET("v1/report/foruser/{userId}")
    Call<List<ReportDto>> getReportsWithUserId(@Path("userId") int userId);

    // Not using @Delete : doesn't allow to have a body
    @HTTP(method = "DELETE", path = "v1/report", hasBody = true)
    Call<Object> deleteReport(@Body ReportDto reportDto);

    @POST("v1/report")
    Call<Object> postReport(@Body ReportDto reportDto);

    @GET("v1/event/forreport/{reportId}")
    Call<List<EventDto>> getEventsWithReportId(@Path("reportId") int reportId);

    @POST("v1/user")
    Call<UserDto> postUser(@Body UserDto userDto);

    @POST("v1/login")
    Call<String> log(@Body LoginDto loginDto);

    @GET("v1/reportType")
    Call<List<ReportTypeDto>> getReportTypes();

    @POST("v1/event")
    Call<Object> postEvent(@Body EventDto eventDto);
}

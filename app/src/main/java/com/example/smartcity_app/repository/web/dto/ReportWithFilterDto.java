package com.example.smartcity_app.repository.web.dto;

import com.squareup.moshi.Json;

import java.util.List;

public class ReportWithFilterDto {
    @Json(name = "countWithoutLimit")
    private Integer counts;

    @Json(name = "data")
    private List<ReportDto> reportDtos;

    public ReportWithFilterDto(Integer counts, List<ReportDto> reportDtos) {
        this.counts = counts;
        this.reportDtos = reportDtos;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public List<ReportDto> getReportDtos() {
        return reportDtos;
    }

    public void setReportDtos(List<ReportDto> reportDtos) {
        this.reportDtos = reportDtos;
    }

    @Override
    public String toString() {
        return "ReportWithFilterDto{" +
                "countWithoutLimit=" + counts +
                ", data='" + reportDtos +
                '}';
    }
}

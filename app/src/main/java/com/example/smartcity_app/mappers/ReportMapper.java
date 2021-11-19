package com.example.smartcity_app.mappers;

import com.example.smartcity_app.model.Report;
import com.example.smartcity_app.repositories.web.dto.ReportDto;

import java.util.ArrayList;
import java.util.List;

public class ReportMapper {
    private static ReportMapper instance = null;

    private ReportMapper() {
    }

    public static ReportMapper getInstance() {
        if (instance == null) {
            instance = new ReportMapper();
        }
        return instance;
    }

    public Report mapToReport(ReportDto dto) {
        if (dto == null) {
            return null;
        }

        Report report = new Report(dto.getId(), dto.getDescription(), dto.getState(), dto.getCity(), dto.getStreet(), dto.getZipCode(), dto.getHouseNumber(), dto.getCreationDate(), dto.getReporter(), dto.getReportType());

        return report;
    }

    public List<Report> mapToReports(List<ReportDto> reportDtos) {
        if (reportDtos == null) {
            return null;
        }

        List<Report> reports = new ArrayList<>();

        for(ReportDto reportDto: reportDtos){
            reports.add(mapToReport(reportDto));
        }

        return reports;
    }
}

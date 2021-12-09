package com.example.smartcity_app.service.mappers;

import com.example.smartcity_app.model.ReportType;
import com.example.smartcity_app.repository.web.dto.ReportTypeDto;

import java.util.ArrayList;
import java.util.List;

public class ReportTypeMapper {
    private static ReportTypeMapper instance = null;

    private ReportTypeMapper() {}

    public static ReportTypeMapper getInstance() {
        if(instance == null)
            instance = new ReportTypeMapper();
        return instance;
    }

    public ReportType mapToReportType(ReportTypeDto dto) {
        if(dto == null)
            return null;
        ReportType reportType = new ReportType(dto.getId(), dto.getLabel());
        return reportType;
    }

    public List<ReportType> mapToReportTypes(List<ReportTypeDto> dtos) {
        if(dtos == null)
            return null;

        List<ReportType> reportTypes = new ArrayList<>();
        for(ReportTypeDto reportTypeDto: dtos)
            reportTypes.add(mapToReportType(reportTypeDto));

        return reportTypes;
    }
}

package com.example.smartcity_app.repositories.web.dto;

import com.squareup.moshi.Json;

import java.util.Date;

public class EventDto {
    private Integer id;

    @Json(name="date_hour")
    private Date dateHour;

    private Integer duration;
    private String description;

    @Json(name="created_at")
    private Date createdAt;

    @Json(name="report")
    private Integer reportId;

    @Json(name="creator")
    private Integer creatorId;

    public EventDto(Integer id, Date dateHour, Integer duration, String description, Date createdAt, Integer reportId, Integer creatorId) {
        this.id = id;
        this.dateHour = dateHour;
        this.duration = duration;
        this.description = description;
        this.createdAt = createdAt;
        this.reportId = reportId;
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id=" + id +
                ", dateHour=" + dateHour + '\'' +
                ", duration=" + duration + '\'' +
                ", description=" + description + '\'' +
                ", createdAt=" + createdAt + '\'' +
                ", report=" + reportId + '\'' +
                ", creator=" + creatorId + '\'' +
                "}";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateHour() {
        return dateHour;
    }

    public void setDateHour(Date dateHour) {
        this.dateHour = dateHour;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }
}

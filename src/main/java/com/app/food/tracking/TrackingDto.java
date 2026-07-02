package com.app.food.tracking;



import java.time.LocalDateTime;

public class TrackingDto {
    public Long orderId;
    public String status;

    public String agentName;
    public String agentPhone;
    public String vehicleNo;

    public Double lat;
    public Double lng;
    public LocalDateTime lastUpdated;
}
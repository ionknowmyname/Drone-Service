package com.faithfulolaleru.DroneService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneBatteryLogResponse {

    private String id;
    private String droneSerial;
    private Integer batteryPercentage;
    private LocalDateTime createdAt;
}

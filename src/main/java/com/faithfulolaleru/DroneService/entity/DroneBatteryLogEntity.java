package com.faithfulolaleru.DroneService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "drone_battery_log_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DroneBatteryLogEntity {

    @Id
    private String id;

    @Column(name = "drone_serial")
    private String droneSerial;

    @Column(name = "battery_percentage")
    private Integer batteryPercentage;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;


}

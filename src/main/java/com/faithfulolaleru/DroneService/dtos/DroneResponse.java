package com.faithfulolaleru.DroneService.dtos;

import com.faithfulolaleru.DroneService.enums.DroneModel;
import com.faithfulolaleru.DroneService.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneResponse {

    private String serial;
    private DroneModel model;
    private Integer weight;
    private Integer batteryCapacity;
    private DroneState state;
}

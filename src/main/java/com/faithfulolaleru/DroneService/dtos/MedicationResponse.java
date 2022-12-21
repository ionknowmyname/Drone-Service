package com.faithfulolaleru.DroneService.dtos;

import com.faithfulolaleru.DroneService.entity.DroneEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationResponse {

    private String name;
    private Integer weight;
    private String code;
    private String photoLink;
    private DroneEntity drone;
    private String responseMessage;
}

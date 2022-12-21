package com.faithfulolaleru.DroneService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequest {

    private String name;
    private Integer weight;
    private String code;
}

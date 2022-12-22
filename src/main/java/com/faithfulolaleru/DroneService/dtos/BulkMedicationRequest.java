package com.faithfulolaleru.DroneService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkMedicationRequest {

    private List<String> medicationCodes;
}

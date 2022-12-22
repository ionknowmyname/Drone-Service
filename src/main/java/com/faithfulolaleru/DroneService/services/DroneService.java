package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.BulkMedicationRequest;
import com.faithfulolaleru.DroneService.dtos.DroneRequest;
import com.faithfulolaleru.DroneService.dtos.DroneResponse;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;

import java.util.List;

public interface DroneService {

    DroneResponse registerDrone(DroneRequest request);

    DroneResponse getDroneBySerial(String serial);

    List<MedicationResponse> getMedicationsOnDrone(String serial);

    DroneResponse loadMedicationToDrone(String serial, BulkMedicationRequest requestDto);

    DroneResponse unloadMedicationToDrone(String serial, BulkMedicationRequest requestDto);

    String getBatteryPercentOfDrone(String serial);
}

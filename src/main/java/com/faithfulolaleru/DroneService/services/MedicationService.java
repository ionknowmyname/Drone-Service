package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.MedicationRequest;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface MedicationService {

    MedicationResponse createMedication(String name, Integer weight, MultipartFile file);

    String test(MultipartFile file);

    MedicationResponse setDroneForMedication(String medicationCode, DroneEntity entity);

    MedicationResponse unsetDroneForMedication(String medicationCode);

    Optional<MedicationEntity> getMedicationByCode(String code);
}

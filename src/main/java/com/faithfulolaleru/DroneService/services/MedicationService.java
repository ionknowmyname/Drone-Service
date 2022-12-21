package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.MedicationRequest;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MedicationService {

    MedicationResponse createMedication(String request, MultipartFile file);

    String test(MultipartFile file);
}

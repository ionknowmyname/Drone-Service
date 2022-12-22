package com.faithfulolaleru.DroneService.controllers;

import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.response.AppResponse;
import com.faithfulolaleru.DroneService.services.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "api/v1/medication")
@AllArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;


    @PostMapping(value = "/", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
                                                MediaType.APPLICATION_JSON_VALUE })
    public AppResponse<?> createMedication(@RequestPart(value = "request") String request,
                                           @RequestPart(value = "file") MultipartFile file) {

        MedicationResponse response = medicationService.createMedication(request, file);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    @PostMapping("/test")
    public AppResponse<?> createFileTest(@RequestPart(value = "file") MultipartFile file) {

        String response = medicationService.test(file);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }
}

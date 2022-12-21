package com.faithfulolaleru.DroneService.controllers;

import com.faithfulolaleru.DroneService.dtos.DroneRequest;
import com.faithfulolaleru.DroneService.dtos.DroneResponse;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.response.AppResponse;
import com.faithfulolaleru.DroneService.services.DroneService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/drone")
@AllArgsConstructor
public class DroneController {

    private final DroneService droneService;


    @GetMapping("/id/{serial}")
    public AppResponse<?> getDroneBySerial(@PathVariable("serial") String serial) {

        DroneResponse response = droneService.getDroneBySerial(serial);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @GetMapping("/id/{serial}/medications")
    public AppResponse<?> getMedicationsOnDrone(@PathVariable("serial") String serial) {

        List<MedicationResponse> response = droneService.getMedicationsOnDrone(serial);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @PostMapping("/")
    public AppResponse<?> registerDrone(@RequestBody DroneRequest requestDto) {

        DroneResponse response = droneService.registerDrone(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }
}

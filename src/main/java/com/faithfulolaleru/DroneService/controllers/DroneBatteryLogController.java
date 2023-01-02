package com.faithfulolaleru.DroneService.controllers;

import com.faithfulolaleru.DroneService.dtos.DroneBatteryLogResponse;
import com.faithfulolaleru.DroneService.dtos.DroneResponse;
import com.faithfulolaleru.DroneService.entity.DroneBatteryLogEntity;
import com.faithfulolaleru.DroneService.response.AppResponse;
import com.faithfulolaleru.DroneService.services.DroneBatteryLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/droneBatteryLog")
@AllArgsConstructor
public class DroneBatteryLogController {

    private final DroneBatteryLogService droneBatteryLogService;


    @GetMapping("/id/{serial}")
    public AppResponse<?> getAllBatteryLogsByBatterySerial(@PathVariable("serial") String serial) {

        List<DroneBatteryLogResponse> response = droneBatteryLogService.getAllBatteryLogsByDroneSerial(serial);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }
}

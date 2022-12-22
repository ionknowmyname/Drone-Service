package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.entity.DroneBatteryLogEntity;
import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.repository.DroneBatteryLogRepository;
import com.faithfulolaleru.DroneService.repository.DroneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DroneBatteryLogService {

    private final DroneBatteryLogRepository droneBatteryLogRepository;

    private final DroneRepository droneRepository;


    public boolean saveDroneBatteryLog() {

        List<DroneEntity> allDrones = droneRepository.findAll();

        List<DroneBatteryLogEntity> toSave = allDrones.stream()
                .map(drone -> buildDroneBatteryLogEntity(drone))
                .collect(Collectors.toList());

        List<DroneBatteryLogEntity> saved = droneBatteryLogRepository.saveAll(toSave);

        return true;
    }

    private DroneBatteryLogEntity buildDroneBatteryLogEntity(DroneEntity entity) {

        return DroneBatteryLogEntity.builder()
            .id(UUID.randomUUID().toString())
            .droneSerial(entity.getSerial())
            .batteryPercentage(entity.getBatteryCapacity())
            .createdAt(LocalDateTime.now())
            .build();
    }
}

package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.DroneRequest;
import com.faithfulolaleru.DroneService.dtos.DroneResponse;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.exception.ErrorResponse;
import com.faithfulolaleru.DroneService.exception.GeneralException;
import com.faithfulolaleru.DroneService.repository.DroneRepository;
import com.faithfulolaleru.DroneService.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final ModelMapper modelmapper;

    @Override
    public DroneResponse registerDrone(DroneRequest request) {
        DroneEntity droneEntity = buildDroneEntity(request);
        boolean canSave = AppUtils.validateDroneEntityToSave(droneEntity);

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Invalid Request, Unable to Create Drone");
        }

        DroneEntity savedEntity = droneRepository.save(droneEntity);
        DroneResponse response = modelmapper.map(savedEntity, DroneResponse.class);

        return response;
    }

    @Override
    public DroneResponse getDroneBySerial(String serial) {

        return droneRepository.findBySerial(serial)
                .map(e -> modelmapper.map(e, DroneResponse.class))
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, ErrorResponse.ERROR_DRONE,
                        "Drone with serial not found"));
    }

    @Override
    public List<MedicationResponse> getMedicationsOnDrone(String serial) {

        return droneRepository.findBySerial(serial)
                .map(de -> de.getMedications())
                .get().stream().map(me -> modelmapper.map(me, MedicationResponse.class))
                .collect(Collectors.toList());
    }

    public DroneEntity buildDroneEntity(DroneRequest request) {
        return DroneEntity.builder()
                .serial(UUID.randomUUID().toString())
                .model(request.getModel())
                .weight(request.getWeight())
                .batteryCapacity(request.getBatteryCapacity())
                .state(request.getState())
                .build();
    }
}

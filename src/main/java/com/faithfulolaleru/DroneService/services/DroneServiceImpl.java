package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.BulkMedicationRequest;
import com.faithfulolaleru.DroneService.dtos.DroneRequest;
import com.faithfulolaleru.DroneService.dtos.DroneResponse;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;
import com.faithfulolaleru.DroneService.enums.DroneState;
import com.faithfulolaleru.DroneService.exception.ErrorResponse;
import com.faithfulolaleru.DroneService.exception.GeneralException;
import com.faithfulolaleru.DroneService.repository.DroneRepository;
import com.faithfulolaleru.DroneService.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final MedicationService medicationService;

    private final ModelMapper modelmapper;

    @Override
    public DroneResponse registerDrone(DroneRequest request) {

        // prepare to create
        DroneEntity droneEntity = buildDroneEntity(request);
        boolean canSave = AppUtils.validateDroneEntityToSave(droneEntity);

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Invalid Request, Unable to Create Drone");
        }

        DroneEntity savedEntity = droneRepository.save(droneEntity);

        return modelmapper.map(savedEntity, DroneResponse.class);
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
                .map(DroneEntity::getMedications)
                .get().stream().map(me -> modelmapper.map(me, MedicationResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DroneResponse loadMedicationToDrone(String serial, BulkMedicationRequest requestDto) {

        Optional<DroneEntity> foundDrone = droneRepository.findBySerial(serial);

        List<MedicationEntity> medicationsToLoad = requestDto.getMedicationCodes().stream()
                .map(c -> medicationService.getMedicationByCode(c).get())
                .collect(Collectors.toList());

        DroneEntity droneToBeLoaded = mountOnDrone(foundDrone.get(), medicationsToLoad);
        boolean canSave = AppUtils.validateDroneEntityToSave(droneToBeLoaded);
        boolean batteryGood = AppUtils.validateDroneToLoad(droneToBeLoaded);   // validate battery over 25%

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Invalid Request, Unable to load Drone");
        }
        if(!batteryGood) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Battery too low to load up drone");
        }

        DroneEntity savedEntity = droneRepository.save(droneToBeLoaded);
        DroneResponse response = modelmapper.map(savedEntity, DroneResponse.class);


        // update drone details for all medication entities uploaded to drone
        requestDto.getMedicationCodes().forEach(code -> medicationService.setDroneForMedication(code, savedEntity));


        return response;
    }

    @Override
    @Transactional
    public DroneResponse unloadMedicationToDrone(String serial, BulkMedicationRequest requestDto) {

        Optional<DroneEntity> foundDrone = droneRepository.findBySerial(serial);

        List<MedicationEntity> medicationsToUnload = requestDto.getMedicationCodes().stream()
                .map(c -> medicationService.getMedicationByCode(c).get())
                .collect(Collectors.toList());

        DroneEntity droneToBeUnloaded = unmountFromDrone(foundDrone.get(), medicationsToUnload);

        boolean canSave = AppUtils.validateDroneEntityToSave(droneToBeUnloaded);

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Invalid Request, Unable to load Drone");
        }

        DroneEntity savedEntity = droneRepository.save(droneToBeUnloaded);
        DroneResponse response = modelmapper.map(savedEntity, DroneResponse.class);


        // update drone details for all medication entities uploaded to drone
        requestDto.getMedicationCodes().forEach(code -> medicationService.unsetDroneForMedication(code));


        return response;
    }

    @Override
    public String getBatteryPercentOfDrone(String serial) {
        Integer batteryCapacity = droneRepository.findBySerial(serial).get().getBatteryCapacity();

        return "Battery percentage of Drone is --> {}" + batteryCapacity;
    }

    private DroneEntity buildDroneEntity(DroneRequest request) {
        return DroneEntity.builder()
                .serial(UUID.randomUUID().toString())
                .model(request.getModel())
                .weight(request.getWeight())
                .batteryCapacity(request.getBatteryCapacity())
                .state(request.getState())
                .build();
    }

    private DroneEntity mountOnDrone(DroneEntity dEntity, List<MedicationEntity> mEntities) {

        Integer weightSum = mEntities.stream().map(MedicationEntity::getWeight)
                .reduce(0, Integer::sum);
        // .reduce(0, (a, b) -> a + b);

        dEntity.setWeight(dEntity.getWeight() + weightSum);
        dEntity.setState(DroneState.LOADING);
        dEntity.setMedications(mEntities);

        return dEntity;
    }

    private DroneEntity unmountFromDrone(DroneEntity dEntity, List<MedicationEntity> mEntities) {

        Integer weightSum = mEntities.stream().map(MedicationEntity::getWeight)
                .reduce(0, Integer::sum);
        // .reduce(0, (a, b) -> a + b);

        dEntity.setWeight(dEntity.getWeight() - weightSum);
        dEntity.setState(DroneState.IDLE);

        // remove only medications in the list, leave the rest there
        List<MedicationEntity> currentMedications = dEntity.getMedications();

        boolean wasRemoved = currentMedications.removeAll(mEntities);
        List<MedicationEntity> medicationsRemaining = currentMedications.stream()
                .filter(m -> !mEntities.contains(m))
                .collect(Collectors.toList());

        if(wasRemoved) {
            dEntity.setMedications(currentMedications);
        } else {
            dEntity.setMedications(medicationsRemaining);
        }

        return dEntity;
    }

    private Integer returnDroneBatteryPercentage(DroneEntity entity) {
        return null;
    }


}

package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.BulkMedicationRequest;
import com.faithfulolaleru.DroneService.dtos.DroneRequest;
import com.faithfulolaleru.DroneService.dtos.DroneResponse;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;
import com.faithfulolaleru.DroneService.enums.DroneModel;
import com.faithfulolaleru.DroneService.enums.DroneState;
import com.faithfulolaleru.DroneService.exception.GeneralException;
import com.faithfulolaleru.DroneService.repository.DroneRepository;
import com.faithfulolaleru.DroneService.utils.AppUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {DroneServiceImpl.class, ModelMapper.class})
class DroneServiceImplTest {

    @Autowired
    private DroneService droneService;

    @MockBean
    private MedicationService medicationService;

    @MockBean
    private DroneRepository droneRepository;

//    @MockBean
//    private AppUtils appUtils;

    private ModelMapper modelMapper = new ModelMapper();


    @Test
    void registerDroneSuccessful() {

        DroneEntity entity = getDroneEntity();
        DroneRequest request  = getDroneRequest();

        // skip validation, assume DroneEntity is valid

        when(droneRepository.save(any())).thenReturn(entity);

        DroneResponse testResponse = modelMapper.map(entity, DroneResponse.class);

        DroneResponse serviceResponse = droneService.registerDrone(request);

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.getSerial()).isEqualTo(testResponse.getSerial());

    }

    @Test
    void makeSureValidateWorks() {

        DroneRequest request  = getDroneRequest();

        //  when(AppUtils.validateDroneEntityToSave(getDroneEntity())).thenReturn(false);

        try (MockedStatic<AppUtils> utilities = Mockito.mockStatic(AppUtils.class)) {

            utilities.when(() -> AppUtils.validateDroneEntityToSave(getDroneEntity()))
                    .thenReturn(false);

            Exception exception = assertThrows(GeneralException.class, () -> droneService.registerDrone(request));

            assertEquals("Invalid Request, Unable to Create Drone", exception.getMessage());
        }

    }

    @Test
    void loadMedicationToDroneSuccessfully() {

        BulkMedicationRequest bulkRequest = getBulkMedicationRequest();

        when(droneRepository.findBySerial(anyString())).thenReturn(Optional.of(getDroneEntity()));

        List<MedicationEntity> medicationsToLoad = new ArrayList<>();
        for (Integer i = 1; i <= bulkRequest.getMedicationCodes().size(); i++) {
            when(medicationService.getMedicationByCode(anyString()))
                    .thenReturn(Optional.of(getMedicationEntity("XYZ-" + i, i.toString().repeat(6))));

            medicationsToLoad.add(getMedicationEntity("XYZ-" + i, i.toString().repeat(6)));
        }

        // Skip validations again, do them seperately

        when(droneRepository.save(any())).thenReturn(getMountedDroneEntity());

        DroneResponse testResponse = modelMapper.map(getMountedDroneEntity(), DroneResponse.class);

        DroneResponse serviceResponse = droneService.loadMedicationToDrone("ae013960-701b-4442-a125-23c157f4df45", bulkRequest);

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.getSerial()).isEqualTo(testResponse.getSerial());
        assertThat(serviceResponse.getMedications()).isNotNull();

        // assert that medication entity is update to include drone, do it in medication service test
    }

    @Test
    void loadMedicationToDroneFailed_BatteryTooLow() {

        BulkMedicationRequest bulkRequest = getBulkMedicationRequest();

        when(droneRepository.findBySerial(anyString())).thenReturn(Optional.of(getDroneEntity()));

        List<MedicationEntity> medicationsToLoad = new ArrayList<>();
        for (Integer i = 1; i <= bulkRequest.getMedicationCodes().size(); i++) {
            when(medicationService.getMedicationByCode(anyString()))
                    .thenReturn(Optional.of(getMedicationEntity("XYZ-" + i, i.toString().repeat(6))));

            medicationsToLoad.add(getMedicationEntity("XYZ-" + i, i.toString().repeat(6)));
        }

        // fail validation here
        try (MockedStatic<AppUtils> utilities = Mockito.mockStatic(AppUtils.class)) {

            utilities.when(() -> AppUtils.validateDroneEntityToSave(getMountedDroneEntity()))
                    .thenReturn(true);

            utilities.when(() -> AppUtils.validateDroneToLoad(getMountedDroneEntity()))
                    .thenReturn(false);

            Exception exception = assertThrows(GeneralException.class,
                    () -> droneService.loadMedicationToDrone("ae013960-701b-4442-a125-23c157f4df45", bulkRequest));

            assertEquals("Battery too low to load up drone", exception.getMessage());
        }
    }

    @Test
    void loadMedicationToDroneFailed_MaxWeightExceeded() {

        BulkMedicationRequest bulkRequest = getBulkMedicationRequest();

        when(droneRepository.findBySerial(anyString())).thenReturn(Optional.of(getDroneEntity()));

        List<MedicationEntity> medicationsToLoad = new ArrayList<>();
        for (Integer i = 1; i <= bulkRequest.getMedicationCodes().size(); i++) {
            when(medicationService.getMedicationByCode(anyString()))
                    .thenReturn(Optional.of(getMedicationEntity("XYZ-" + i, i.toString().repeat(6))));

            medicationsToLoad.add(getMedicationEntity("XYZ-" + i, i.toString().repeat(6)));
        }

        // fail validation here
        try (MockedStatic<AppUtils> utilities = Mockito.mockStatic(AppUtils.class)) {

            utilities.when(() -> AppUtils.validateDroneEntityToSave(getDroneEntity()))
                    .thenReturn(false);

            Exception exception = assertThrows(GeneralException.class,
                    () -> droneService.loadMedicationToDrone("ae013960-701b-4442-a125-23c157f4df45", bulkRequest));

            assertEquals("Invalid Request, Unable to load Drone", exception.getMessage());
        }
    }

    @Test
    void makeSureLoadedDroneReturnsAllMedicationsOnboard() {

        when(droneRepository.findBySerial(anyString())).thenReturn(Optional.of(getMountedDroneEntity()));

        List<MedicationResponse> testResponse = getMountedDroneEntity().getMedications()
                                        .stream().map(me -> entityToDto(me)).collect(Collectors.toList());

        List<MedicationResponse> serviceResponse = droneService.getMedicationsOnDrone("ae013960-701b-4442-a125-23c157f4df45");

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.get(1).getCode()).isEqualTo(testResponse.get(1).getCode());
    }

    private DroneEntity getDroneEntity() {
        return DroneEntity.builder()
                .serial("ae013960-701b-4442-a125-23c157f4df45")
                .model(DroneModel.LIGHTWEIGHT)
                .weight(100)
                .batteryCapacity(80)
                .state(DroneState.IDLE)
                .build();
    }

    private DroneEntity getMountedDroneEntity() {

        List<MedicationEntity> medicationEntities = new ArrayList<>();
        medicationEntities.add(getMedicationEntity("XYZ-1", "111111"));
        medicationEntities.add(getMedicationEntity("XYZ-2", "222222"));
        medicationEntities.add(getMedicationEntity("XYZ-3", "333333"));

        return DroneEntity.builder()
                .serial("ae013960-701b-4442-a125-23c157f4df45")
                .model(DroneModel.LIGHTWEIGHT)
                .weight(160)
                .batteryCapacity(80)
                .state(DroneState.LOADING)
                .medications(medicationEntities)
                .build();
    }

    private DroneRequest getDroneRequest() {
        DroneRequest request = new DroneRequest();
        request.setModel(DroneModel.LIGHTWEIGHT);
        request.setWeight(100);
        request.setBatteryCapacity(80);
        request.setState(DroneState.IDLE);

        return request;
    }

    private DroneResponse getDroneResponse() {
        DroneResponse response = new DroneResponse();
        response.setSerial("ae013960-701b-4442-a125-23c157f4df45");
        response.setModel(DroneModel.LIGHTWEIGHT);
        response.setWeight(100);
        response.setBatteryCapacity(80);
        response.setState(DroneState.IDLE);
        response.setMedications(null);

        return response;
    }

    private BulkMedicationRequest getBulkMedicationRequest() {
        BulkMedicationRequest request = new BulkMedicationRequest();

        List<String> medicationCodes = List.of("111111", "222222", "333333");

        request.setMedicationCodes(medicationCodes);

        return  request;
    }

    private MedicationEntity getMedicationEntity(String id, String code) {
        return MedicationEntity.builder()
                .id(id)
                .name("DRUG1")
                .weight(20)
                .code(code)
                .photoLink("/")
                .build();
    }

    private MedicationResponse entityToDto(MedicationEntity entity) {
        MedicationResponse response = new MedicationResponse();
        BeanUtils.copyProperties(entity, response);

        return response;
    }
}
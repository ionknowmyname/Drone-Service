package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.DroneResponse;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;
import com.faithfulolaleru.DroneService.enums.DroneModel;
import com.faithfulolaleru.DroneService.enums.DroneState;
import com.faithfulolaleru.DroneService.exception.GeneralException;
import com.faithfulolaleru.DroneService.repository.MedicationRepository;
import com.faithfulolaleru.DroneService.utils.AppUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MedicationServiceImpl.class, ModelMapper.class})
class MedicationServiceImplTest {

    @Autowired
    private MedicationService medicationService;

    @MockBean
    private MedicationRepository medicationRepository;

    private ModelMapper modelMapper = new ModelMapper();



    @Test
    void createMedicationSuccessfully() {

        MultipartFile file = getMultiPartFile();

        when(medicationRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Skip validations, do seperately

        when(medicationRepository.save(any())).thenReturn(getMedicationEntity());

        MedicationResponse testResponse = modelMapper.map(getMedicationEntity(), MedicationResponse.class);

        MedicationResponse serviceResponse = medicationService.createMedication("DRUG1", 20, file);

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.getName()).isEqualTo(testResponse.getName());
        assertThat(serviceResponse.getPhotoLink()).isNotNull();  // just to make sure picture uploaded
    }

    @Test
    void makeSureValidationsWork() {

        MultipartFile file = getMultiPartFile();

        try (MockedStatic<AppUtils> utilities = Mockito.mockStatic(AppUtils.class)) {

            utilities.when(() -> AppUtils.generateRandomString(10))
                    .thenReturn("0123456789");

            utilities.when(() -> AppUtils.validateMedicationEntityToSave(any(MedicationEntity.class)))
                    .thenReturn(false);

            Exception exception = assertThrows(GeneralException.class,
                    () -> medicationService.createMedication("DRUG1", 20, file));

            assertEquals("Invalid Request, Unable to Create Medication", exception.getMessage());
        }
    }

    @Test
    void createMedicationFailed_MedicationAlreadyExists() {

        MultipartFile file = getMultiPartFile();

        when(medicationRepository.findByName(anyString())).thenReturn(Optional.of(getMedicationEntity()));

        // MedicationResponse serviceResponse = medicationService.createMedication("DRUG1", 20, file);

        Exception exception = assertThrows(GeneralException.class,
                () -> medicationService.createMedication("DRUG1", 20, file));

        assertEquals("Medication already exists, cannot create anew", exception.getMessage());
    }

    @Test
    void setDroneForMedicationSuccessfully() {

        when(medicationRepository.findByCode(anyString())).thenReturn(Optional.of(getMedicationEntity()));

        when(medicationRepository.save(any())).thenReturn(getMedicationEntityWithDrone());

        MedicationResponse testResponse = modelMapper.map(getMedicationEntityWithDrone(), MedicationResponse.class);

        MedicationResponse serviceResponse = medicationService.setDroneForMedication("XYZ1", getDroneEntity());

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.getDrone()).isNotNull();
        assertThat(testResponse.getDrone()).isNotNull();
        assertThat(serviceResponse.getCode()).isEqualTo(testResponse.getCode());
    }

    @Test
    void setDroneForMedicationFailed_MedicationAlreadyOnDifferentDrone() {

        when(medicationRepository.findByCode(anyString())).thenReturn(Optional.of(getMedicationEntityWithDrone()));

        MedicationResponse serviceResponse = medicationService.setDroneForMedication("XYZ1", getDroneEntity());

        assertThat(serviceResponse).isNull();
    }

    private MedicationEntity getMedicationEntity() {
        return MedicationEntity.builder()
                .id("445445678")
                .name("DRUG1")
                .weight(20)
                .code("XYZ1")
                .photoLink("/test")
                .build();
    }
    private MedicationEntity getMedicationEntityWithDrone() {
        return MedicationEntity.builder()
                .id("445445678")
                .name("DRUG1")
                .weight(20)
                .code("XYZ1")
                .photoLink("/test")
                .drone(getDroneEntity())
                .build();
    }

    private MultipartFile getMultiPartFile() {
        MultipartFile file;
        /*
            try {
                file = new MockMultipartFile("pexels.jpg", new FileInputStream("C:/Users/Faithful/Pictures/pexels.jpg"));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        */

        Path path = Paths.get("C:/Users/Faithful/Pictures/pexels.jpg");
        String name = "pexels.jpg";
        String originalFileName = "pexels.jpg";
        String contentType = "image/jpeg";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        file = new MockMultipartFile(name, originalFileName, contentType, content);

        return file;
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

}
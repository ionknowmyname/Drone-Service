package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.MedicationRequest;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;
import com.faithfulolaleru.DroneService.exception.ErrorResponse;
import com.faithfulolaleru.DroneService.exception.GeneralException;
import com.faithfulolaleru.DroneService.repository.MedicationRepository;
import com.faithfulolaleru.DroneService.utils.AppUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MedicationServiceImpl implements MedicationService {

    // @Value("${file.upload-dir}")
    private static String filePath = "/Users/Faithful/Pictures/forJava/";

    private final MedicationRepository medicationRepository;

    private final ModelMapper modelMapper;

//    public MedicationServiceImpl(String filePath, MedicationRepository medicationRepository,
//                              ModelMapper modelMapper){
//        this.filePath = filePath;
//        this.medicationRepository = medicationRepository;
//        this.modelMapper = modelMapper;
//    }


    @Override
    public MedicationResponse createMedication(String request, MultipartFile file) {

        ObjectMapper mapper = new ObjectMapper();
        MedicationRequest requestDto;
        String photoLink = "";
        String responseMessage = "No file was uploaded";

        // for file
        try {
            if(!file.isEmpty()) {
                photoLink = filePath + file.getOriginalFilename();

                file.transferTo(new File(photoLink));

                responseMessage = "file successfully uploaded to '" + photoLink + "'";
            }
        } catch (IOException ex) {
            log.error("Error while mapping to class --> {}", ex.getMessage());
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Error while uploading your file");
        }


        // for requestDto
        try{
            requestDto = mapper.readValue(request, MedicationRequest.class);
        } catch (JsonProcessingException e) {
            log.error("Error while mapping to class --> {}", e.getMessage());
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Check your request again");
        }


        // check if medication exists before attempting to create new medication
        Optional<MedicationEntity> foundMedication = medicationRepository.findByName(requestDto.getName());
        if(foundMedication.isPresent()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Medication already exists, cannot create anew");
        }

        // prepare to create
        MedicationEntity medicationEntity = buildMedicationEntity(requestDto, photoLink);
        boolean canSave = AppUtils.validateMedicationEntityToSave(medicationEntity);

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Invalid Request, Unable to Create Medication");
        }

        MedicationEntity savedEntity = medicationRepository.save(medicationEntity);
        MedicationResponse response = modelMapper.map(savedEntity, MedicationResponse.class);
        response.setResponseMessage(responseMessage);


        return response;
    }

    @Override
    public String test(MultipartFile file) {
        return null;
    }

    @Override
    public MedicationResponse setDroneForMedication(String medicationCode, DroneEntity entity) {
        MedicationEntity foundMedication = medicationRepository.findByCode(medicationCode)
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, ErrorResponse.ERROR_MEDICATION,
                        "Medication with code not found"));

        foundMedication.setDrone(entity);

        MedicationEntity savedEntity = medicationRepository.save(foundMedication);

        return modelMapper.map(savedEntity, MedicationResponse.class);

        // use Update @Query in repo and add @Transactional
        // try it for unsettingDrone
    }

    @Override
    public MedicationResponse unsetDroneForMedication(String medicationCode) {
        MedicationEntity foundMedication = medicationRepository.findByCode(medicationCode)
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, ErrorResponse.ERROR_MEDICATION,
                        "Medication with code not found"));

        foundMedication.setDrone(null);

        MedicationEntity savedEntity = medicationRepository.save(foundMedication);

        return modelMapper.map(savedEntity, MedicationResponse.class);
    }

    @Override
    public Optional<MedicationEntity> getMedicationByCode(String code) {
        return medicationRepository.findByCode(code);
    }

    public MedicationEntity buildMedicationEntity(MedicationRequest request, String photoLink) {
        return MedicationEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .code(AppUtils.generateRandomString(10))
                .photoLink(photoLink)
                .build();
    }
}

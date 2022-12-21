package com.faithfulolaleru.DroneService.services;

import com.faithfulolaleru.DroneService.dtos.MedicationRequest;
import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;
import com.faithfulolaleru.DroneService.exception.ErrorResponse;
import com.faithfulolaleru.DroneService.exception.GeneralException;
import com.faithfulolaleru.DroneService.repository.MedicationRepository;
import com.faithfulolaleru.DroneService.utils.AppUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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

    @Value("${file.upload-dir}")
    private String filePath;

    private final MedicationRepository medicationRepository;

    private final ModelMapper modelMapper;


    @Override
    public MedicationResponse createMedication(String request, MultipartFile file) {

        ObjectMapper mapper = new ObjectMapper();
        MedicationRequest requestDto;
        String photoLink = "";
        String responseMessage = "No file was uploaded";

        try {
            if(!file.isEmpty()) {
                photoLink = filePath + file.getOriginalFilename();

                file.transferTo(new File(photoLink));

                responseMessage = "file successfully uploaded to '" + photoLink + "'";
            }
        } catch (IOException ex) {
            log.error("Error while mapping to class --> ", ex.getMessage());
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Error while uploading your file");
        }


        try{
            requestDto = mapper.readValue(request, MedicationRequest.class);

        } catch (JsonMappingException e) {
            log.error("Error while mapping to class --> ", e.getMessage());
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Check your request again");
        } catch (JsonProcessingException e) {
            log.error("Error while mapping to class --> ", e.getMessage());
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Check your request again");
        }


        // check if medication exists before attempting to create new medication
        Optional<MedicationEntity> foundMedication = medicationRepository.findByCode(requestDto.getCode());
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

    public MedicationEntity buildMedicationEntity(MedicationRequest request, String photoLink) {
        return MedicationEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .code(request.getCode())
                .photoLink(photoLink)
                .build();
    }
}

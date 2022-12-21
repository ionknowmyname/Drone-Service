package com.faithfulolaleru.DroneService.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {


    public static final String ERROR_DRONE = "DRONE_ERROR_ENCOUNTERED";

    public static final String ERROR_MEDICATION = "MEDICATION_ERROR_ENCOUNTERED";

    public static final String ERROR_CRON = "CRON_JOB_ERROR_ENCOUNTERED";




    private String error;
    private String message;
    private HttpStatus httpStatus;


    public ErrorResponse(final GeneralException ex) {
        this.httpStatus = ex.getHttpStatus();
        this.error = ex.getError();
        this.message = ex.getMessage();   //  ex.getLocalizedMessage()
    }
}

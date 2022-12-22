package com.faithfulolaleru.DroneService.utils;

import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;

import java.util.Locale;


public class AppUtils {

    public static boolean validateMedicationName(MedicationEntity entity) {

        if(!entity.getName().matches("^[A-Za-z0-9_-]*$")) {
            return false;
        }

        return true;
    }

    public static boolean validateMedicationCode(MedicationEntity entity) {

        String toUpper = entity.getCode().toUpperCase(Locale.ROOT);
        if(toUpper.matches("^[A-Z0-9_]*$")) {
            return true;
        }

        return false;
    }

    public static boolean validateDroneEntityToSave(DroneEntity entity) {
        if(entity.getBatteryCapacity() < 0 || entity.getBatteryCapacity() > 100
                || entity.getWeight() > 500) {

            return false;
        }
        return true;
    }

    public static boolean validateDroneToLoad(DroneEntity entity) {
        if(entity.getBatteryCapacity() < 25) {
            return false;
        }
        return true;
    }

    public static boolean validateMedicationEntityToSave(MedicationEntity entity) {
        if(!validateMedicationName(entity) || !validateMedicationCode(entity)) {
            return false;
        }
        return true;
    }
}

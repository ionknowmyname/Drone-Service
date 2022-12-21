package com.faithfulolaleru.DroneService.utils;

import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;


public class AppUtils {

    public static boolean validateMedicationName(MedicationEntity entity) {
//        TODO: more validations

        return true;
    }

    public static boolean validateMedicationCode(MedicationEntity entity) {
        //        TODO: more validations

        return true;
    }

    public static boolean validateDroneEntityToSave(DroneEntity entity) {
        if(entity.getBatteryCapacity() < 0 || entity.getBatteryCapacity() > 100
                || entity.getWeight() > 500) {

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

package com.faithfulolaleru.DroneService.utils;

import com.faithfulolaleru.DroneService.entity.DroneEntity;

public class AppUtils {

    public boolean validateMedicationName(String name) {
        return true;
    }

    public boolean validateMedicationCode(String code) {
        return true;
    }

    public static boolean validateDroneEntityToSave(DroneEntity entity) {
        if(entity.getBatteryCapacity() < 0 || entity.getBatteryCapacity() > 100
                || entity.getWeight() > 500) {

            return false;
        }
        return true;
    }
}

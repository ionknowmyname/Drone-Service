package com.faithfulolaleru.DroneService.utils;

import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.entity.MedicationEntity;
import lombok.NoArgsConstructor;

import java.util.Locale;

@NoArgsConstructor
public class AppUtils {

    public static boolean validateMedicationName(MedicationEntity entity) {

        if(entity.getName().matches("^[A-Za-z0-9_-]*$")) {
            return true;
        }

        return false;
    }

    public static boolean validateMedicationCode(MedicationEntity entity) {

        // String toUpper = entity.getCode().toUpperCase(Locale.ROOT);

        if(entity.getCode().matches("^[A-Z0-9_]*$")) {
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

    public static String generateRandomString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789" + "abcdefghijklmnopqrstuvxyz" + "_";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}

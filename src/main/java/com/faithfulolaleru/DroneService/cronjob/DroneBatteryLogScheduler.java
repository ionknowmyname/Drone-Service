package com.faithfulolaleru.DroneService.cronjob;

import com.faithfulolaleru.DroneService.services.DroneBatteryLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DroneBatteryLogScheduler {

    private final DroneBatteryLogService droneBatteryLogService;



    // @Scheduled(cron = "0 0/5 * * * ?") // 5 minutes for testing
    @Scheduled(cron = "0 0 0/3 * * ?")  //every 3 hours
    public void saveBatteryLog() {

        droneBatteryLogService.saveDroneBatteryLog();
    }
}

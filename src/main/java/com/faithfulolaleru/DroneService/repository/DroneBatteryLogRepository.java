package com.faithfulolaleru.DroneService.repository;

import com.faithfulolaleru.DroneService.entity.DroneBatteryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneBatteryLogRepository extends JpaRepository<DroneBatteryLogEntity, String> {

    // @Query("SELECT d FROM drone_battery_log_table AS d WHERE d.droneSerial = ?1")
    List<DroneBatteryLogEntity> findAllByDroneSerial(String droneSerial);
}

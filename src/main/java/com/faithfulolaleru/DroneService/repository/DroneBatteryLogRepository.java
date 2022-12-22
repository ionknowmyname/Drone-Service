package com.faithfulolaleru.DroneService.repository;

import com.faithfulolaleru.DroneService.entity.DroneBatteryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneBatteryLogRepository extends JpaRepository<DroneBatteryLogEntity, String> {

    List<DroneBatteryLogEntity> findAllByDroneSerial(String droneSerial);
}

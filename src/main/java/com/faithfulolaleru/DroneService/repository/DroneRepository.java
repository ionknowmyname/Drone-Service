package com.faithfulolaleru.DroneService.repository;

import com.faithfulolaleru.DroneService.entity.DroneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroneRepository extends JpaRepository<DroneEntity, String> {


    Optional<DroneEntity> findBySerial(String serial);
}

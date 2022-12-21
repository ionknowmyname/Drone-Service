package com.faithfulolaleru.DroneService.repository;

import com.faithfulolaleru.DroneService.entity.DroneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, String> {


    Optional<DroneEntity> findBySerial(String serial);
}

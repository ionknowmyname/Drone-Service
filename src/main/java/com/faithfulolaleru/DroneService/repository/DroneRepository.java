package com.faithfulolaleru.DroneService.repository;

import com.faithfulolaleru.DroneService.entity.DroneEntity;
import com.faithfulolaleru.DroneService.enums.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, String> {


    Optional<DroneEntity> findBySerial(String serial);

    List<DroneEntity> findAllByState(DroneState state);
}

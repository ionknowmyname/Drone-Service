package com.faithfulolaleru.DroneService.repository;

import com.faithfulolaleru.DroneService.entity.MedicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<MedicationEntity, String> {


    Optional<MedicationEntity> findByCode(String code);

    Optional<MedicationEntity> findByName(String name);
}

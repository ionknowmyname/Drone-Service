package com.faithfulolaleru.DroneService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "medication_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicationEntity {

    @Id
    private String id;

    private String name;

    private Integer weight;

    private String code;

    @ManyToOne
    @JoinColumn(name = "drone_serial")
    private DroneEntity drone;
}

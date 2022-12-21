package com.faithfulolaleru.DroneService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "medication_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicationEntity {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "code")
    private String code;

    @Column(name = "photo_link")
    private String photoLink;

    @ManyToOne
    @JoinColumn(name = "drone_serial")
    private DroneEntity drone;
}

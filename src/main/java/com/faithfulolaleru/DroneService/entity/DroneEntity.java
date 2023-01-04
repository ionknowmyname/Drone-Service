package com.faithfulolaleru.DroneService.entity;

import com.faithfulolaleru.DroneService.dtos.MedicationResponse;
import com.faithfulolaleru.DroneService.enums.DroneModel;
import com.faithfulolaleru.DroneService.enums.DroneState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "drone_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DroneEntity {

    @Id
    private String serial;

    @Enumerated(EnumType.STRING)
    @Column(name = "drone_model")
    private DroneModel model;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "drone_state")
    private DroneState state;

    @Builder.Default
    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL)
    private List<MedicationEntity> medications = new ArrayList<>();

}

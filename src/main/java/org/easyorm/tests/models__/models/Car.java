package org.easyorm.tests.models__.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.easyorm.modelsmapper.annotations.Column;
import org.easyorm.modelsmapper.annotations.Id;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Car {

    @Id(autoIncrement = true)
    private Long id;
    private Integer doorsCount;
    @Column(name = "car_brand", length = 50)
    private String brand;
    private Integer kilometrage;

    // relations
    private Person owner;
    private List<Driver> drivers;

}

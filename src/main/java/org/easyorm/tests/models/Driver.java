package org.easyorm.tests.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Driver extends Person {
    private Long id;
    private String typePermit;
    private Date dateOptention;

    // relations
    private List<Car> drivingCars;
}

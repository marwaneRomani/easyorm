package org.easyorm.tests.newModels.models2;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Car extends Vehicle {
    private Integer numDoors;

    private List<Rental> rentals;
}

package org.easyorm.tests.newModels.models2;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Vehicle {
    protected Integer id;
    protected String model;
    protected String manufacturer;
}

package org.easyorm.tests.newModels.models2;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Driver {
    private Integer id;
    private String name;

    private License license;
    private List<Rental> rentals;
}

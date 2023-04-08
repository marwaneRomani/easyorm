package org.easyorm.tests.newModels.models2;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Rental {
    private Integer id;
    private Date startDate;
    private Date endDate;

    private Driver driver;
    private Car car;
}

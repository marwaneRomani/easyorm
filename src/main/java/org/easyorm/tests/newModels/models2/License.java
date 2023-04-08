package org.easyorm.tests.newModels.models2;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class License {
    private Integer id;
    private String type;
    private Date expiration;

    private Driver driver;
}

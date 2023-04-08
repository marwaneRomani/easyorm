package org.easyorm.tests.newModels.models0;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payment {
    private Integer id;
    private Date date;
    private Integer amount;

    private Order order;
}
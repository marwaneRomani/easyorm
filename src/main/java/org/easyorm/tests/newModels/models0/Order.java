package org.easyorm.tests.newModels.models0;


import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private Integer id;
    private Date date;

    private List<OrderItem> orderItems;
    private Payment payment;
}

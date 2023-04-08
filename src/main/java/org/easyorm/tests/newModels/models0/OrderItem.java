package org.easyorm.tests.newModels.models0;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {
    private Integer id;
    private Integer quantity;

    private Order order;
    private Product product;
}

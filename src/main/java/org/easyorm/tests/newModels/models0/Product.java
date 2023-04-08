package org.easyorm.tests.newModels.models0;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private Integer id;
    private String name;
    private Float price;

    private List<OrderItem> orderItems;
}
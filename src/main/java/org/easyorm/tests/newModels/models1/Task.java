package org.easyorm.tests.newModels.models1;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Task {
    private Integer id;
    private String name;

    private List<Employee> employees;
}

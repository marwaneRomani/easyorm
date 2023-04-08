package org.easyorm.tests.newModels.models1;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project {
    private Integer id;
    private String name;

    private List<Employee> employees;
    private List<Client> clients;
}
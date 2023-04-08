package org.easyorm.tests.newModels.models1;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Employee extends Person {
    private Integer salary;
    private Date hireDate;

    private Department department;
    private List<Project> projects;
    private List<Task> tasks;
}
package org.easyorm.tests.newModels.models1;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Client extends Person {
    private Float balance;

    private List<Project> projects;
}
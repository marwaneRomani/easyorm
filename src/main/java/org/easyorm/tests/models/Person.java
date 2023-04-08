package org.easyorm.tests.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.easyorm.modelsmapper.annotations.Ignore;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Person {
    protected String nic;
    protected String firstName;
    protected String lastName;
    protected Date birthDay;
    @Ignore
    protected Integer age;

    // relations
    protected List<Car> cars;
}

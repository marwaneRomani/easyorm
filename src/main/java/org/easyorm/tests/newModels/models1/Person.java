package org.easyorm.tests.newModels.models1;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class Person {
    protected Integer id;
    protected String name;
}

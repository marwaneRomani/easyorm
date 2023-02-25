package org.orm.models;

import lombok.Data;

public @Data class A {
    private Integer id;
    private String nameA;
    private B b;
}

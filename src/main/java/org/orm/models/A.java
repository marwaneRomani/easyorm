package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
public @Data class A {
    private Integer id;
    private Team team;

    public A(Integer id) {
        this.id = id;
    }
}

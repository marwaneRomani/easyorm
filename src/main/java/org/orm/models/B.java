package org.orm.models;

import lombok.Data;

import java.util.List;

public @Data class B {
    private Integer id;
    private String nameB;
    private List<A> as;
}

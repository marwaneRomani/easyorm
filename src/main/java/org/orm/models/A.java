package org.orm.models;

import lombok.Data;
import org.orm.framework.ModelsMapper.Annotations.Id;

public @Data class A {
    @Id(autoIncrement = true)
    private Long idLong;
    private String nameA;
    private B b;
}

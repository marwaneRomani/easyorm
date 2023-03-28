package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class ChefFilliere {
    private Long id;
    private String name;

    private Filiere filiere;
}

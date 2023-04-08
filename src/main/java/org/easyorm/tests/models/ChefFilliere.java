package org.easyorm.tests.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class ChefFilliere {
    private Long id;
    private String name;

    private List<Filiere> filieres;

}

package org.easyorm.tests.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Filiere {
    private String nom;
    private String description;

    private List<Userr> students;

    // many to many with user for test
    private List<Userr> participatedUsers;

    private ChefFilliere chefFilliere;

    public Filiere(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }


}

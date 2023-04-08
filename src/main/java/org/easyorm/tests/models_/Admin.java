package org.easyorm.tests.models_;

public class Admin extends Personne {
    private String nonDepartement;

    public String getNonDepartement() {
        return nonDepartement;
    }

    public void setNonDepartement(String nonDepartement) {
        this.nonDepartement = nonDepartement;
    }
}

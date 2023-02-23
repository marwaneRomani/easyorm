package org.orm.models;

import org.orm.framework.ModelsMapper.Annotations.Column;

import java.util.List;

public class City {
    @Column(length = 50)
    private String name;

    private List<Person> citoyennes;

    private Team team;

    public City() {

    }

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getCitoyennes() {
        return citoyennes;
    }

    public void setCitoyennes(List<Person> citoyennes) {
        this.citoyennes = citoyennes;
    }

}

package org.orm.models;

import org.orm.framework.ModelsMapper.Annotations.Column;

import java.util.List;

public class Team {
    private Long id_team;
    @Column(nullable = false)
    private String name;

    private City city;

    private List<Player> players;

    public Team() {
    }

    public Team(Long id_team, String name) {
        this.id_team = id_team;
        this.name = name;
    }

    public Long getId_team() {
        return id_team;
    }

    public void setId_team(Long id_team) {
        this.id_team = id_team;
    }



    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
package org.orm.models;


import org.orm.framework.ModelsMapper.Annotations.Id;

public class Player extends Person {
    @Id(name = "player_id", autoIncrement = true)
    private Long id;

    private Byte number;

    private Team team;

    public Player() { }
    public Player(Long id, Byte number, String full_name, String cin) {
        this.id = id;
        this.number = number;
        super.full_name = full_name;
        super.cin = cin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getNumber() {
        return number;
    }

    public void setNumber(Byte number) {
        this.number = number;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}

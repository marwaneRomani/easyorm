package org.orm;

import org.orm.framework.DataMapper.DataMapper;
import org.orm.framework.OrmApplication;
import org.orm.models.City;
import org.orm.models.Player;
import org.orm.models.Team;

public class Main {
    public static void main(String[] args) {
        //OrmApplication.run();

        Player player = DataMapper.buildObject(Player.class)
                .showModel();

    }
}
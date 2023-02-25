package org.orm.framework;

import org.orm.framework.ApplicationState.ApplicationState;
import org.orm.framework.DataMapper.DataMapper;
import org.orm.framework.DatabaseBuilder.DatabaseBuilder;
import org.orm.framework.ModelsMapper.ModelsMapper;

// the facade for the client
public class OrmApplication {

    public static void run() {
        try {
            ApplicationState state = ApplicationState.getState("/personal_data/sideProjects/orm/orm/src/main/java/org/orm/config.json");

            ModelsMapper.map(state.getModelsPath());

            String strategy = state.getStrategy();
            String dialect  = state.getDialect();

            DatabaseBuilder databaseBuilder = new DatabaseBuilder();
            //databaseBuilder.build(strategy, dialect);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

package org.orm.framework;

import org.orm.framework.ApplicationState.ApplicationState;
import org.orm.framework.ConnectionsPool.ConnectionPool;
import org.orm.framework.DataMapper.DataMapper;
import org.orm.framework.DatabaseBuilder.DatabaseBuilder;
import org.orm.framework.ModelsMapper.ModelsMapper;

import java.net.URL;
import java.sql.Connection;

// the facade for the client
public class OrmApplication {

    public static void run() {
        try {
            ApplicationState state = ApplicationState.getState();
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(), state.getUsername(), state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection;

            ModelsMapper.map(state.getModelsPath());

            String strategy = state.getStrategy();
            String dialect  = state.getDialect();

            connection = pool.getConnection();

            DatabaseBuilder databaseBuilder = new DatabaseBuilder();
            databaseBuilder.build(strategy, dialect, connection);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

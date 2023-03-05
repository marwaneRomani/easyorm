package org.orm.framework;

import org.orm.framework.ApplicationState.ApplicationState;
import org.orm.framework.ConnectionsPool.ConnectionPool;
import org.orm.framework.DataMapper1.ObjectBuilder;
import org.orm.framework.DatabaseBuilder.DatabaseBuilder;
import org.orm.framework.ModelsMapper.ModelsMapper;

import java.sql.Connection;

// the facade for the client
public class OrmApplication {
    public static void run() {
        try {
            ApplicationState state = ApplicationState.getState();
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(), state.getUsername(), state.getPassword(), state.getConnectionPoolMaxSize());

            ModelsMapper.map(state.getModelsPath());

            String strategy = state.getStrategy();
            String dialect  = state.getDialect();

            Connection connection = pool.getConnection();

            DatabaseBuilder databaseBuilder = new DatabaseBuilder();
            databaseBuilder.build(strategy, dialect, connection);

            pool.releaseConnection(connection);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static <T> ObjectBuilder<T> buildObject(Class<T> model) {
        return new ObjectBuilder<>(model);
    }

}

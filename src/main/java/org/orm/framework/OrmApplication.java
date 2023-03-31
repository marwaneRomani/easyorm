package org.orm.framework;

import org.orm.framework.ApplicationState.ApplicationState;
import org.orm.framework.ConnectionsPool.ConnectionPool;
import org.orm.framework.DataMapper1.ObjectBuilder;
import org.orm.framework.DatabaseBuilder.DatabaseBuilder;
import org.orm.framework.ModelsMapper.ModelsMapper;

import java.sql.Connection;

public class OrmApplication {
    private static boolean modelsMapped;

    public static void run() {
        try {
            ApplicationState state = ApplicationState.getState();
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(), state.getUsername(), state.getPassword(), state.getConnectionPoolMaxSize());

            mapModels();

            String strategy = state.getStrategy();
            String dialect  = state.getDialect();
            Connection connection = pool.getConnection();

            DatabaseBuilder databaseBuilder = new DatabaseBuilder();

            if (!strategy.equals("update"))
                databaseBuilder.build(strategy, dialect, connection);

            pool.releaseConnection(connection);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> ObjectBuilder<T> buildObject(Class<T> model) {
        // builder pattern
        mapModels();
        return new ObjectBuilder<>(model);
    }

    private static void mapModels() {
        if (!modelsMapped) {
            ApplicationState state = ApplicationState.getState();
            ModelsMapper.map(state.getModelsPath());
            modelsMapped = true;
        }
    }
}

package org.easyorm;

import org.easyorm.applicationstate.ApplicationState;
import org.easyorm.connectionpool.ConnectionPool;
import org.easyorm.datamapper.ObjectBuilder;
import org.easyorm.databasebuilder.DatabaseBuilder;
import org.easyorm.entitiesdtasource.EntitiesDataSource;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.ModelsMapper;

import java.sql.Connection;
import java.util.Map;

public class EasyOrm {
    private static boolean modelsMapped;

    public static void bootstrap() {
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

    public static <T> ObjectBuilder<T> buildObject(Class<T> model, Boolean transactional) {
        // builder pattern
        mapModels();
        return new ObjectBuilder<>(model, transactional);
    }


    private static void mapModels() {
        if (!modelsMapped) {
            ApplicationState state = ApplicationState.getState();
            ModelsMapper.map(state.getModelsPath());
            modelsMapped = true;
        }
    }
}

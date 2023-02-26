package org.orm.framework.DataMapper.ObjectBuilders;

import org.orm.framework.ApplicationState.ApplicationState;
import org.orm.framework.ConnectionsPool.ConnectionPool;
import org.orm.framework.DataMapper.ObjectBuilders.find.FindBuilder;
import org.orm.framework.DataMapper.ObjectBuilders.find.RelationEvaluator;
import org.orm.framework.DataMapper.ObjectBuilders.save.SaveBuilder;
import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ObjectBuilder<T> {
    private Class<?> model;
    private T object;
    private Entity entity;
    private ApplicationState state;

    public ObjectBuilder(Class<?> model) {
        this.model = model;
        this.state = ApplicationState.getState();
        try {
            object = (T) Class.forName(model.getName()).newInstance();
            entity = EntitiesDataSource.getModelsSchemas().get(model.getSimpleName());


            if (entity == null) {
                throw new Exception("entity not found.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * persistence methods
    * */

    public T save(T object) {
        // pool connection
        ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());

        SaveBuilder<T> saveBuilder = new SaveBuilder<>(entity);
        List<Query> queries = saveBuilder.save(object);

        try {
            Connection connection = pool.getConnection();
            // jdbc Template
            pool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return object;
    }

    public List<T> findAll() {
        FindBuilder findBuilder = new FindBuilder(entity);

        Query query = findBuilder.findAll();

        return null;
    }


    public ObjectBuilder<T> findById(Object id) {
        FindBuilder findBuilder = new FindBuilder(entity);
        Query byId = findBuilder.findById(id);

        System.out.println(byId.getQuery());
        // execute the query

        return this;
    }


    public ObjectBuilder<T> findOne() {
        return this;
    }


    public ObjectBuilder<T> get(String relationName) {
        RelationEvaluator<T> evaluator = new RelationEvaluator<>(entity,relationName, object);
        evaluator.evaluate();
        return this;
    }

    public T build(List<Query> queries) {
        return object;
    }
}

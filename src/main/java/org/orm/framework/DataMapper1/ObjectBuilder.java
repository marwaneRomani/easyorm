package org.orm.framework.DataMapper1;

import org.orm.framework.ApplicationState.ApplicationState;
import org.orm.framework.ConnectionsPool.ConnectionPool;
import org.orm.framework.DataMapper.JdbcTemplate.JdbcTemplateImpl;
import org.orm.framework.DataMapper.ObjectBuilders.Query;
import org.orm.framework.DataMapper.ObjectBuilders.find.FindBuilder;
import org.orm.framework.DataMapper1.methods.save.Save;
import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.TransactionsManager.Transaction;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

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
     *
     **/

    public T save(T object) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Save<T> saveObject = new Save<>(new JdbcTemplateImpl(connection));


            Method save = Save.class.getMethod("save", Entity.class, Object.class);
            Transaction.wrapMethodInTransaction(connection, saveObject, save, entity, object);

            pool.releaseConnection(connection);

            return object;

        } catch (SQLException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public T find(T object) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();


            pool.releaseConnection(connection);

            return object;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public T findById(Object id) {
        FindBuilder findBuilder = new FindBuilder(entity);
        Query byId = findBuilder.findById(id);

        System.out.println(byId.getQuery());
        // execute the query

        return null;
    }



}

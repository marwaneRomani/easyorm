package org.easyorm.datamapper.methods.update;

import org.easyorm.EasyOrm;
import org.easyorm.applicationstate.ApplicationState;
import org.easyorm.connectionpool.ConnectionPool;
import org.easyorm.datamapper.jdbctemplate.JdbcTemplate;
import org.easyorm.datamapper.jdbctemplate.JdbcTemplateImpl;
import org.easyorm.datamapper.methods.save.Save;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.transactionsmanager.Transaction;

import java.lang.reflect.Method;
import java.sql.Connection;

public class Update<T> {

    private JdbcTemplate template;

    public Update(JdbcTemplateImpl template) {
        this.template = template;
    }

    public T updateById(Entity entity, Object id ,T newObject) {
        Transaction.wrapMethodInTransaction(() -> {
            Object foundObject = (T) EasyOrm
                    .buildObject(entity.getModel(), false)
                    .findById(id)
                    .buildObject();

            EasyOrm
                    .buildObject(entity.getModel())
                    .deleteById(id);

            try {
                ApplicationState state = ApplicationState.getState();

                ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
                Connection connection = pool.getConnection();

                Save<T> saveObject = new Save<>(new JdbcTemplateImpl(connection));
                Method saveMethod = saveMethod = Save.class.getMethod("save", Entity.class, Object.class);
                saveMethod.invoke(saveObject, entity, newObject);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return newObject;
    }

}
package org.orm.framework.datamapper1.methods.delete;

import lombok.SneakyThrows;
import org.orm.framework.OrmApplication;
import org.orm.framework.customexception.ORMException;
import org.orm.framework.datamapper1.jdbctemplate.JdbcTemplate;
import org.orm.framework.datamapper1.methods.Query;
import org.orm.framework.entitiesdtasource.Entity;

public class Delete<T> {

    private DeleteUtils<T> deleteUtils = new DeleteUtils();
    private JdbcTemplate template;

    public Delete(JdbcTemplate template) {
        this.template = template;
    }

    public T deleteById(Entity entity, Object id) {
        Query query = deleteUtils.deleteById(entity, id);

        T foundObject = (T) OrmApplication
                .buildObject(entity.getModel())
                .findById(id)
                .buildObject();

        if (foundObject != null) {
            try {
                int result = template.nonQuery(query.getQuery(), query.getValues());

                if (result > 0)
                    return foundObject;

                throw new ORMException("unable to delete this object .");
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                throw new ORMException("no " + entity.getName() + " with the id " + id);
            } catch (ORMException e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}

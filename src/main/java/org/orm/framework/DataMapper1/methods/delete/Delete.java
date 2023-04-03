package org.orm.framework.DataMapper1.methods.delete;

import org.orm.framework.DataMapper1.JdbcTemplate.JdbcTemplate;
import org.orm.framework.DataMapper1.methods.Query;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.OrmApplication;
import org.orm.framework.customException.ORMException;

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

        try {
            if (foundObject == null)
                throw new ORMException("no " + entity.getName() + " with the id " + id);

            int result = template.nonQuery(query.getQuery(), query.getValues());

            if (result > 0)
                return foundObject;

            throw new ORMException("unable to delete this object .");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

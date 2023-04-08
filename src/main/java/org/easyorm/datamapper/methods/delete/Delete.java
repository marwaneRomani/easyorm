package org.easyorm.datamapper.methods.delete;

import org.easyorm.EasyOrm;
import org.easyorm.customexception.ORMException;
import org.easyorm.datamapper.jdbctemplate.JdbcTemplate;
import org.easyorm.datamapper.queryBuilders.Query;
import org.easyorm.entitiesdtasource.Entity;

public class Delete<T> {

    private DeleteUtils<T> deleteUtils = new DeleteUtils();
    private JdbcTemplate template;

    public Delete(JdbcTemplate template) {
        this.template = template;
    }

    public T deleteById(Entity entity, Object id) {
        Query query = deleteUtils.deleteById(entity, id);

        T foundObject = (T) EasyOrm
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

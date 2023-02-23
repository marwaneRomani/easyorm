package org.orm.framework.DataMapper.ObjectBuilder;

import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;

import java.util.List;

public class ObjectBuilder<T> {
    private Class<?> model;
    private T object;
    private Entity entity;

    public ObjectBuilder(Class<?> model) {
        this.model = model;
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


    /*
    *  persistence methods
    * */

    public ObjectBuilder<T> save(T object) {
        SaveBuilder<T> saveBuilder = new SaveBuilder<>(entity);

        saveBuilder.save(object);

        return this;
    }


    public ObjectBuilder findById(Object id) {

        return this;
    }

    public List<T> findAll() {

        return null;
    }

    public T build() {

        return object;
    }

    public T showModel() {

        return object;
    }
}

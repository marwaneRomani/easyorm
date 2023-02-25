package org.orm.framework.DataMapper.ObjectBuilders;

import org.orm.framework.DataMapper.ObjectBuilders.find.FindBuilder;
import org.orm.framework.DataMapper.ObjectBuilders.save.SaveBuilder;
import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;

import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder<T> {
    private Class<?> model;
    private T object;
    private Entity entity;

    private List<Query> queries;

    public ObjectBuilder(Class<?> model) {
        this.model = model;
        try {

            object = (T) Class.forName(model.getName()).newInstance();
            entity = EntitiesDataSource.getModelsSchemas().get(model.getSimpleName());
            queries = new ArrayList<>();

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
        SaveBuilder<T> saveBuilder = new SaveBuilder<>(entity);

        // build queries
        SaveBuilder<T> save = saveBuilder.save(object);
        List<Query> queries = save.getQueries();

        this.build(queries);

        return object;
    }


    public ObjectBuilder<T> findById(Object id) {
        FindBuilder<T> findBuilder = new FindBuilder<>(entity);
        findBuilder.findById(id);

        return this;
    }

    public List<T> findAll() {
        FindBuilder<T> findBuilder = new FindBuilder(entity);

        return null;
    }

    public T build(List<Query> queries) {

        return object;
    }

    public T showModel() {

        return object;
    }
}

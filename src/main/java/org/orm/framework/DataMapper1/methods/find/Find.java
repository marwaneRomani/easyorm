package org.orm.framework.DataMapper1.methods.find;

import org.orm.framework.DataMapper1.JdbcTemplate.JdbcTemplate;
import org.orm.framework.DataMapper1.methods.Query;
import org.orm.framework.EntitiesDataSource.Entity;

import java.util.List;


public class Find<T> {

    private FindUtils<T> findUtils = new FindUtils<>();
    private JdbcTemplate template;

    public Find(JdbcTemplate template) {
        this.template = template;
    }


    public List<T> findAll(Entity entity) {
        Query findAllQuery = findUtils.findAll(entity);

        try {
             return (List<T>) template.queryForList(findAllQuery.getQuery(), entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T findById(Entity entity, Object id) {
        Query findByIdQuery = findUtils.findById(entity, id);

        try {
            return (T) template.queryForObject(findByIdQuery.getQuery(), entity, new Object[]{id});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T findOne(Entity entity, List<String> keys, List<String> conditionTypes , List<Object> values, List<String> chain) {
        Query findQuery = findUtils.find(entity, keys, conditionTypes, values, chain, 0);
        try {
            return (T) template.queryForObject(findQuery.getQuery(),entity, findQuery.getValues());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> findMany(Entity entity, List<String> keys,List<String> conditionTypes ,List<Object> values, List<String> chain, Integer limit) {
        Query findQuery = findUtils.find(entity, keys, conditionTypes ,values, chain, limit);

        try {
            return (List<T>) template.queryForList(findQuery.getQuery(), entity,findQuery.getValues());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}


package org.orm.framework.DataMapper1.methods.find;

import org.orm.framework.DataMapper.JdbcTemplate.JdbcTemplate;
import org.orm.framework.DataMapper.QueryBuilders.SelectQueryBuilder;
import org.orm.framework.DataMapper1.methods.Query;
import org.orm.framework.EntitiesDataSource.Entity;

import java.util.List;
import java.util.Map;


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

    public T findOne(Entity entity, List<String> keys, List<Object> values) {
        Query findByIdQuery = findUtils.findOne(entity, keys, values);

        try {
            return (T) template.queryForObject(findByIdQuery.getQuery(),entity, findByIdQuery.getValues());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}


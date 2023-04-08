package org.easyorm.datamapper.methods.find;

import org.easyorm.datamapper.jdbctemplate.JdbcTemplate;
import org.easyorm.datamapper.queryBuilders.Query;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.fieldsmapper.relation.Relation;

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

    public T findOne(Entity entity, List<String> keys, List<String> conditionTypes, List<Object> values, List<String> chain) {
        Query findQuery = findUtils.find(entity, keys, conditionTypes, values, chain, 0);
        try {
            return (T) template.queryForObject(findQuery.getQuery(), entity, findQuery.getValues());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> findMany(Entity entity, List<String> keys, List<String> conditionTypes, List<Object> values, List<String> chain, Integer limit) {
        Query findQuery = findUtils.find(entity, keys, conditionTypes, values, chain, limit);

        try {
            return (List<T>) template.queryForList(findQuery.getQuery(), entity, findQuery.getValues());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object findForeignKey(Entity entity, Object primaryKey, Entity searchedEntity, String foreignKeyName) {
        Query subQuery = findUtils.findForeignKey(entity, primaryKey, foreignKeyName);
        Query searchedRelationQuery = findUtils.findById(searchedEntity, "($)");

        String query = searchedRelationQuery.getQuery().replaceFirst("\\?", "(" + subQuery.getQuery() + ")");

        Object[] subQueryValues = subQuery.getValues();


        try {
            return template.queryForObject(query, searchedEntity, subQueryValues);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> findFromManyToManyRelation(Entity searchedEntity, Entity entity, Relation relation, Object entityPrimaryKeyValue) {
        Query fromManyToManyRelationQuery = findUtils.findFromManyToManyRelation(searchedEntity, entity, relation, entityPrimaryKeyValue);
        Object[] values = fromManyToManyRelationQuery.getValues();

        String query = fromManyToManyRelationQuery.getQuery().replaceFirst("\\?", values[0].toString());
        query = query.replaceFirst("\\?", values[1].toString());

        values = new Object[] { values[2] };

        try {
            return template.queryForList(query, searchedEntity, values);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}


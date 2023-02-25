package org.orm.framework.DataMapper.ObjectBuilders.find;

import org.orm.framework.DataMapper.ObjectBuilders.Query;
import org.orm.framework.DataMapper.QueryBuilders.SelectQueryBuilder;
import org.orm.framework.DataMapper.Utils.FindAttribute;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;

import java.util.ArrayList;
import java.util.List;

public class FindBuilder {
    private Entity entity;

    public FindBuilder(Entity entity) {
        this.entity = entity;
    }

    public Query findById(Object id) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());
        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));
        selectQueryBuilder.addCondition(entity.getPrimaryKey().getName(), id);

        Query query = selectQueryBuilder.build();

        return query;
    }

    public Query findAll() {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());
        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));

        Query query = selectQueryBuilder.build();

        return query;
    }

    public Query findSingleAttribute(String attributeName, Object value) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());
        selectQueryBuilder.addColumn(attributeName);
        selectQueryBuilder.addCondition(entity.getPrimaryKey().getName(), value);

        Query query = selectQueryBuilder.build();

        return query;
    }


    public Query findSingleRelation() {

        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();
        selectQueryBuilder.setTable(entity.getName());


        return null;
    }

    public Query findByCondition(String condition, Object value) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();
        selectQueryBuilder.setTable(entity.getName());
        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));
        selectQueryBuilder.addCondition(condition, value);

        Query query = selectQueryBuilder.build();

        return query;
    }

    public Query findFromManyTables(String otherTable, Condition conditions) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();
        selectQueryBuilder.setTable(entity.getName());
        selectQueryBuilder.setTable(otherTable);

        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));

        for (int i = 0; i < conditions.getContions().length; i++) {
            selectQueryBuilder.addCondition(conditions.getContions()[i], conditions.getValues()[i]);
        }

        Query query = selectQueryBuilder.build();

        return query;
    }

    public Query findListRelation(String relationName) {

        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();
        selectQueryBuilder.setTable(entity.getName());


        return null;
    }

}








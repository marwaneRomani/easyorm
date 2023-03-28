package org.orm.framework.DataMapper1.methods.find;

import org.orm.framework.DataMapper.QueryBuilders.SelectQueryBuilder;
import org.orm.framework.DataMapper1.methods.Query;
import org.orm.framework.EntitiesDataSource.Entity;

import java.util.List;

public class FindUtils<T> {

    public Query findAll(Entity entity) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());

        selectQueryBuilder.addColumn(entity.getPrimaryKey().getName());
        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));

        Query query = selectQueryBuilder.build();

        return query;
    }


    public Query findById(Entity entity, Object id) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());
        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));
        selectQueryBuilder.addCondition(entity.getPrimaryKey().getName(), id);

        Query query = selectQueryBuilder.build();

        return query;
    }

    public Query findOne(Entity entity, List<String> keys, List<Object> values) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());
        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));

        for (int i = 0; i < keys.size(); i++)
            selectQueryBuilder.addCondition(keys.get(i), values.get(i));


        Query query = selectQueryBuilder.build();

        return query;
    }


}

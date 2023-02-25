package org.orm.framework.DataMapper.ObjectBuilders.find;

import org.orm.framework.DataMapper.ObjectBuilders.Query;
import org.orm.framework.DataMapper.QueryBuilders.SelectQueryBuilder;
import org.orm.framework.EntitiesDataSource.Entity;

import java.util.ArrayList;
import java.util.List;

public class FindBuilder<T> {
    private Entity entity;

    private List<Query> queries;

    public FindBuilder(Entity entity) {
        this.entity = entity;
        queries = new ArrayList<>();
    }

    public void findById(Object id) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());
        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));
        selectQueryBuilder.addCondition(entity.getPrimaryKey().getName(), id);

        Query query = selectQueryBuilder.build();
        queries.add(query);
    }

    public void get(String relationName) {

    }

}

package org.orm.framework.DataMapper.ObjectBuilder;

import org.orm.framework.DataMapper.QueriyBuilders.InsertQueryBuilder;
import org.orm.framework.DataMapper.Utils.GettersInvoke;
import org.orm.framework.EntitiesDataSource.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveBuilder<T> {
    private Entity entity;
    private InsertQueryBuilder insertQueryBuilder;

    private Map<String,Object[]> queries;

    private Boolean isNormalAttributeQuesryBuild;

    public SaveBuilder(Entity entity) {
        this.entity = entity;
        this.insertQueryBuilder = new InsertQueryBuilder(entity.getName());
        this.queries = new HashMap();
        this.isNormalAttributeQuesryBuild = false;
    }

    public SaveBuilder<T> save(T objectToPersist) {
        if (isNormalAttributeQuesryBuild)
            return this;

        saveNormalAttrQuery(objectToPersist);
        isNormalAttributeQuesryBuild = true;


        return this;
    }

    private void saveNormalAttrQuery(T objectToPersist) {
        if (!entity.getPrimaryKey().isAutoIncrement()) {
            insertQueryBuilder.column(
                    entity.getPrimaryKey().getName()
            ).value(
                    GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), objectToPersist)
            );
        }

        // specify the columns and values to insert
        entity.getNormalAttributes()
                .forEach(attribute -> {
                    insertQueryBuilder.column(
                            attribute.getName()
                    ).value(
                            GettersInvoke.getAttributeValue(attribute, objectToPersist)
                    );
                });

        String sql = insertQueryBuilder.build();
        Object[] values = insertQueryBuilder.getValues();

        queries.put(sql, values);
    }

    private void saveRelationQuery(T objectToPersist) {
        
    }
}

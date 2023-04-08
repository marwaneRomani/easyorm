package org.easyorm.datamapper.methods.delete;

import org.easyorm.datamapper.queryBuilders.Query;
import org.easyorm.entitiesdtasource.Entity;


public class DeleteUtils<T> {

    public Query deleteById(Entity entity, Object id) {
        org.easyorm.framework.dataMapper1.QueryBuilders.DeleteQueryBuilder deleteQueryBuilder = new org.easyorm.framework.dataMapper1.QueryBuilders.DeleteQueryBuilder(entity.getName());

        deleteQueryBuilder.addEqualCondition(entity.getPrimaryKey().getName(), id);

        Query query = deleteQueryBuilder.build();

        return query;
    }
}

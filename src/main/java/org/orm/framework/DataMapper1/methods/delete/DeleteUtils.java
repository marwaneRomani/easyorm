package org.orm.framework.DataMapper1.methods.delete;

import org.orm.framework.DataMapper1.QueryBuilders.DeleteQueryBuilder;
import org.orm.framework.DataMapper1.methods.Query;
import org.orm.framework.EntitiesDataSource.Entity;

public class DeleteUtils<T> {

    public Query deleteById(Entity entity, Object id) {
        DeleteQueryBuilder deleteQueryBuilder = new DeleteQueryBuilder(entity.getName());

        deleteQueryBuilder.addEqualCondition(entity.getPrimaryKey().getName(), id);

        Query query = deleteQueryBuilder.build();

        return query;
    }
}

package org.easyorm.datamapper.methods.update;

import org.easyorm.datamapper.queryBuilders.Query;
import org.easyorm.datamapper.queryBuilders.UpdateQueryBuilder;
import org.easyorm.entitiesdtasource.Entity;

public class UpdateUtils {

    public Query updateById(Entity entity, Object id) {
        UpdateQueryBuilder updateQueryBuilder = new UpdateQueryBuilder(entity.getName());

        return null;
    }
}

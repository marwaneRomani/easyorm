package org.orm.framework.DataMapper.ObjectBuilders.save;

import org.orm.framework.DataMapper.ObjectBuilders.Query;
import org.orm.framework.DataMapper.Utils.GettersInvoke;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;

import java.util.ArrayList;
import java.util.List;

public class SaveBuilder<T> {
    private SaveUtils<T> saveUtils;

    private Entity entity;
    private Boolean isNormalAttributeQuesryBuild;
    private List<Query> queries;

    public SaveBuilder(Entity entity) {
        this.saveUtils = new SaveUtils<T>();
        this.entity = entity;
        this.queries = new ArrayList<>();
        this.isNormalAttributeQuesryBuild = false;
    }

    public List<Query> save(T objectToPersist) {
        saveNormalAttr(objectToPersist);
        saveRelation(objectToPersist);

        return getQueries();
    }

    private void saveNormalAttr(T objectToPersist) {
        Query query = saveUtils.saveNormalAttrQuery(entity, objectToPersist);
        queries.add(query);
    }

    private void saveRelation(T objectToPersist) {

        for (Attribute attribute : entity.getRelationalAtrributes()) {
            Object attributeValue = GettersInvoke.getAttributeValue(attribute, objectToPersist);
            if (attributeValue != null) {
                List<Query> queryList = saveUtils.saveRelation(entity, attribute, objectToPersist);
                queryList.forEach(query -> queries.add(query));
            }
        }
    }

    public List<Query> getQueries() {
        return queries;
    }
}


















package org.orm.framework.datamapper.ObjectBuilders.save;

import org.orm.framework.datamapper1.methods.Query;
import org.orm.framework.datamapper.Utils.GettersInvoke;
import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;

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


















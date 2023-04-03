package org.orm.framework.datamapper1.methods.save;

import org.orm.framework.datamapper1.jdbctemplate.JdbcTemplate;
import org.orm.framework.datamapper1.methods.Query;
import org.orm.framework.datamapper1.utils.GettersInvoke;
import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;

import java.util.ArrayList;
import java.util.List;

public class Save<T> {
    private SaveUtils<T> saveUtils = new SaveUtils<>();
    private JdbcTemplate template;

    public Save(JdbcTemplate template) {
        this.template = template;
    }

    public void save(Entity entity, T objectToPersist) {

        Query normalAttrQuery = saveNormalAttr(entity, objectToPersist);
        template.nonQuery(normalAttrQuery.getQuery(),
                                             normalAttrQuery.getValues(),
                                             entity,
                                             objectToPersist);

        for (Query query : saveRelations(entity, objectToPersist)) {
            template.nonQuery(query.getQuery(),
                              query.getValues(),
                              entity,
                              objectToPersist);
        }
        // wrap in transaction
    }

    private Query saveNormalAttr(Entity entity, T objectToPersist) {
        return saveUtils.saveNormalAttrQuery(entity, objectToPersist);
    }

    private List<Query> saveRelations(Entity entity, T objectToPersist) {
        List<Query> queries = new ArrayList<>();

        for (Attribute attribute : entity.getRelationalAtrributes()) {
            Object attributeValue = GettersInvoke.getAttributeValue(attribute, objectToPersist);
            if (attributeValue != null) {
                List<Query> queryList = saveUtils.saveRelation(entity, attribute, objectToPersist);
                queryList.forEach(query -> queries.add(query));
            }
        }

        return queries;
    }

}

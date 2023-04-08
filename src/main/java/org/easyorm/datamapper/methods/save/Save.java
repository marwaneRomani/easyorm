package org.easyorm.datamapper.methods.save;

import org.easyorm.datamapper.jdbctemplate.JdbcTemplate;
import org.easyorm.datamapper.queryBuilders.Query;
import org.easyorm.datamapper.utils.GettersInvoke;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.attribute.AttributeList;

import java.lang.reflect.ParameterizedType;
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

    }

    private Query saveNormalAttr(Entity entity, T objectToPersist) {
        return saveUtils.saveNormalAttrQuery(entity, objectToPersist);
    }

    private List<Query> saveRelations(Entity entity, T objectToPersist) {
        List<Query> queries = new ArrayList<>();

        for (Attribute attribute : entity.getRelationalAtrributes()) {
            Object attributeValue = GettersInvoke.getAttributeValue(attribute, objectToPersist);

            String attributeType = (attribute instanceof AttributeList) ? ((AttributeList) attribute).getGenericType() : attribute.getType();
            String objectType = "";

            if (attributeValue instanceof List) {
                if (attributeValue != null && ((List<?>) attributeValue).size() > 0) {
                    Object o = ((List<?>) attributeValue).get(0);
                    objectType = o.getClass().getSimpleName();
                }
            }
            else {
                if (attributeValue != null)
                    objectType = attributeValue.getClass().getSimpleName();
            }

            boolean equals = objectType.equals(attributeType);

            if (attributeValue != null && equals) {
                List<Query> queryList = saveUtils.saveRelation(entity, attribute, objectToPersist);
                queryList.forEach(query -> queries.add(query));
            }
        }

        for (Attribute attribute : entity.getRelationalInheritedAttr()) {
            Object attributeValue = GettersInvoke.getAttributeValue(attribute, objectToPersist);
            if (attributeValue != null) {
                List<Query> queryList = saveUtils.saveRelation(entity, attribute, objectToPersist);
                queryList.forEach(query -> queries.add(query));
            }
        }


        return queries;
    }

}

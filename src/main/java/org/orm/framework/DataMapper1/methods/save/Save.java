package org.orm.framework.DataMapper1.methods.save;

import org.orm.framework.DataMapper.JdbcTemplate.JdbcTemplate;
import org.orm.framework.DataMapper.ObjectBuilders.Query;
import org.orm.framework.DataMapper.Utils.GettersInvoke;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.TransactionsManager.Transaction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Save<T> {
    private SaveUtils<T> saveUtils = new SaveUtils<>();
    private JdbcTemplate template;

    public Save(JdbcTemplate template) {
        this.template = template;
    }

    public void save(Entity entity, T objectToPersist) {

        // wrap in transaction
        Query normalAttrQuery = saveNormalAttr(entity, objectToPersist);
        template.nonQuery(normalAttrQuery.getQuery(),
                                             normalAttrQuery.getValues(),
                                             entity,
                                             objectToPersist);

        if (entity.getPrimaryKey().isAutoIncrement()) {
            //Todo INVOKE SETTER OF THE PRIMARY KEY
        }

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

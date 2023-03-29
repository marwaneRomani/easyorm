package org.orm.framework.DataMapper1.Utils;

import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.customException.ORMException;

public class FindAttribute {

    public static Attribute find(Entity entity, String attributeName) throws ORMException {

        Attribute attribute = entity.getRelationalAtrributes()
                .stream()
                .filter(a -> a.getName().equals(attributeName))
                .findFirst()
                .orElse(null);

        if (attribute == null)
            throw new ORMException("No attribute named " + attributeName + " in the table " + entity.getName());

        return attribute;
    }
}

package org.orm.framework.DataMapper1.Utils;

import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;

public class FindAttribute {

    public static Attribute find(Entity entity, String attributeName) {

        Attribute attribute = entity.getRelationalAtrributes()
                .stream()
                .filter(a -> a.getName().equals(attributeName))
                .findFirst()
                .get();

        // if the attribute is not a relation throw an Exception

        return attribute;
    }
}

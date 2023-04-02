package org.orm.framework.datamapper.Utils;

import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;

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

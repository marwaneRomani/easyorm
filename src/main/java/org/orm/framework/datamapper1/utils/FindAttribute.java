package org.orm.framework.datamapper1.utils;

import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;
import org.orm.framework.customexception.ORMException;

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

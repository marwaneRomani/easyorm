package org.easyorm.datamapper.utils;

import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.customexception.ORMException;

public class FindAttribute {

    public static Attribute find(Entity entity, String attributeName) throws ORMException {

        Attribute attribute = entity.getRelationalAtrributes()
                .stream()
                .filter(a -> a.getName().equals(attributeName))
                .findFirst()
                .orElse(null);

        if (attribute == null) {

            attribute = entity.getRelationalInheritedAttr()
                    .stream()
                    .filter(a -> a.getName().equals(attributeName))
                    .findFirst()
                    .orElse(null);

        }
        if (attribute == null)
            throw new ORMException("No attribute named " + attributeName + " in the table " + entity.getName());

        return attribute;
    }
}

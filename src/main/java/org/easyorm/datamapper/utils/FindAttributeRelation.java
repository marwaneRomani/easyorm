package org.easyorm.datamapper.utils;

import org.easyorm.entitiesdtasource.EntitiesDataSource;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.easyorm.modelsmapper.fieldsmapper.relation.Relation;
import org.easyorm.customexception.ORMException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindAttributeRelation {
    public static Relation find(Attribute attribute, Entity entity) throws ORMException {
        List<Relation> relationList = new ArrayList<>();

        relationList = entity
                .getRelations()
                .stream()
                .filter(r -> r.getOne().equals(attribute) || r.getTwo().equals(attribute))
                .collect(Collectors.toList());

        if (relationList.isEmpty()) {
            relationList = EntitiesDataSource
                    .getModelsSchemas()
                    .get((attribute instanceof AttributeList) ? ((AttributeList) attribute).getGenericType() : attribute.getType() )
                    .getRelations()
                    .stream()
                    .filter(r -> r.getOne().getName().equals(attribute.getName()) || r.getTwo().getName().equals(attribute.getName()))
                    .collect(Collectors.toList());
        }


        Relation relation = relationList.stream()
                .findFirst()
                .orElse(null);

        if (relation == null)
            throw new ORMException("No attribute named " + attribute.getName()  + " in the table " + entity.getName());


        return relation;
    }
}

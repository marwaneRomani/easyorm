package org.orm.framework.datamapper.Utils;

import org.orm.framework.entitiesdtasource.EntitiesDataSource;
import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.orm.framework.modelsmapper.fieldsmapper.relation.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FindAttributeRelation {
    public static Relation find(Attribute attribute, Entity entity) {
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
                .get();

        return relation;
    }
}

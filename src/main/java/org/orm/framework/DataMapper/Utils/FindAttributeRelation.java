package org.orm.framework.DataMapper.Utils;

import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.AttributeList;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.Relation;

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

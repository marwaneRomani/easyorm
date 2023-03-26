package org.orm.framework.ModelsMapper.Mappers;

import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.AttributeList;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.ManyToMany;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.OneToMany;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.OneToOne;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.Relation;

import java.util.List;
import java.util.stream.Collectors;

public class MapRelations {
    private MapRelations() { };

    public static void mapRelations(Entity entity) {

            entity
                .getRelationalAtrributes()
                .forEach(a -> {
                    if (!a.isMapped()) {
                        Relation relation = mapRelationBetweenAtrr(a);
                        entity.subscribeToRelationEvent(relation);
                        if (a instanceof AttributeList)
                            EntitiesDataSource.getModelsSchemas().get(
                                            ((AttributeList) a).getGenericType()
                                    )
                                    .getUnsavedRelationalAttributes()
                                    .add(relation);
                        else
                            EntitiesDataSource.getModelsSchemas().get(a.getType()).getUnsavedRelationalAttributes().add(relation);
                    }
                });
    }


    public static Relation mapRelationBetweenAtrr(Attribute attribute) {
        attribute.mapp(true);

        String referencedEntity = (attribute instanceof AttributeList) ? ((AttributeList) attribute).getGenericType() : attribute.getType();
        List<Attribute> referencedEntityAttrs = EntitiesDataSource
                .getModelsSchemas()
                .get(referencedEntity)
                .getRelationalAtrributes();
        List<Attribute> mappedAttr = referencedEntityAttrs
                .stream()
                .filter(attr -> {
                    String attrType = (attr instanceof AttributeList) ?
                            ((AttributeList) attr).getGenericType() :
                            attr.getType();

                    try {
                        return (attrType.equals(Class
                                .forName(EntitiesDataSource.getEntitiesPath()
                                        + "." + attribute.getClazz().getSimpleName())
                                .getSimpleName()) && !attr.isMapped());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        // exception
        Attribute first = mappedAttr.stream().findFirst().orElse(null);

// TODO throw an exception if first is null
//        if (first == null)
//            throw new Exception();

        first.mapp(true);

        Relation relation;

        if ( attribute instanceof AttributeList )
            if (first instanceof AttributeList )
                relation = new ManyToMany(
                        first.getName().compareTo(attribute.getName()) <= 0 ? first : attribute ,
                        attribute.getName().compareTo(first.getName()) > 0 ? attribute : first
                );
            else
                relation = new OneToMany(first, (AttributeList) attribute);

        else
        if ( first instanceof AttributeList )
            relation = new OneToMany(attribute, (AttributeList) first);
        else
            relation = new OneToOne(attribute, first);


        if (!attribute.getHeritant().isEmpty())
            notifyObservers(attribute.getHeritant(),attribute,first);

        if (!first.getHeritant().isEmpty())
            notifyObservers(first.getHeritant(),first ,attribute);

        return relation;
    }


    public static void notifyObservers(List<Class<?>> observers, Attribute notifier, Attribute other) {
        observers
                .forEach(o -> {
                    EntitiesDataSource
                            .getModelsSchemas()
                            .get(o.getSimpleName())
                            .subscribeToRelationEvent(notifier, other);
                });
    }

}

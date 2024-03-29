package org.easyorm.modelsmapper.mappers;

import org.easyorm.entitiesdtasource.EntitiesDataSource;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.easyorm.modelsmapper.fieldsmapper.relation.ManyToMany;
import org.easyorm.modelsmapper.fieldsmapper.relation.OneToMany;
import org.easyorm.modelsmapper.fieldsmapper.relation.OneToOne;
import org.easyorm.modelsmapper.fieldsmapper.relation.Relation;
import org.easyorm.customexception.ORMException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapRelations {
    private static Entity entity;
    private MapRelations() { };

    public static void mapRelations(Entity entity) {
            MapRelations.entity = entity;


        entity
                .getRelationalAtrributes()
                .forEach(a -> {
                    Map<String, Entity> modelsSchemas = EntitiesDataSource.getModelsSchemas();
                    if (!a.isMapped()) {
                        Relation relation = null;
                        try {
                            relation = mapRelationBetweenAtrr(a);
                        } catch (ORMException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
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


    public static Relation mapRelationBetweenAtrr(Attribute attribute) throws ORMException {
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


        Attribute first = mappedAttr.stream().findFirst().orElse(null);
        if (first == null) {
            throw new ORMException("the mapping relation of this attribute was not set in " +  attribute.getName() + "in the model " + referencedEntity);
        }

        first.mapp(true);

        Relation relation;

        if ( attribute instanceof AttributeList )
            if (first instanceof AttributeList )
                relation = new ManyToMany(
                        first.getName().compareTo(attribute.getName()) <= 0 ? first : attribute ,
                        attribute.getName().compareTo(first.getName()) > 0 ? attribute : first
                );
            else {
                relation = new OneToMany(first, (AttributeList) attribute);
                // register the first attribute as fk of the table
                EntitiesDataSource
                        .getModelsSchemas()
                        .get(referencedEntity)
                        .setForeignKey(first);
            }

        else
            if ( first instanceof AttributeList ) {
                relation = new OneToMany(attribute, (AttributeList) first);
                // register the first attribute as fk of the table
                entity.setForeignKey(attribute);
            }
            else {
                relation = new OneToOne(attribute, first);
                // register the first attribute as fk of the table
                entity.setForeignKey(attribute);
            }


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

package org.easyorm.modelsmapper.mappers;

import org.easyorm.entitiesdtasource.EntitiesDataSource;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.easyorm.modelsmapper.fieldsmapper.primarykey.PrimaryKey;
import org.easyorm.customexception.ORMException;

import java.util.List;
import java.util.stream.Collectors;

public class MapInheretedAttributes {
    public static void mapInheritance(Entity entity) {
        mapPrimaryKey(entity, entity.getSupperClass());

        mapInheritanceNormalAttr(entity, entity.getSupperClass());

        mapInheritanceRelation(entity, entity.getSupperClass());
    }

    public static void mapPrimaryKey(Entity entity, Class<?> supperClass) {
        Entity superClassEntity = EntitiesDataSource
                .getModelsSchemas()
                .get(supperClass.getSimpleName());

        PrimaryKey inheritedPrimaryKey = superClassEntity
                .getPrimaryKey();

        if (superClassEntity.getSupperClass() != Object.class)
            mapPrimaryKey(entity, superClassEntity.getSupperClass());
        else {
            // remove previous primary key
            PrimaryKey oldPrimaryKey = entity.getPrimaryKey();

            Attribute attribute = new Attribute();

            attribute.setName(oldPrimaryKey.getName());
            attribute.setOriginalName(oldPrimaryKey.getOriginalName());
            attribute.setType(oldPrimaryKey.getType());
            attribute.setLength(oldPrimaryKey.getLength());
            attribute.setDbType(oldPrimaryKey.getDbType());

            entity.getNormalAttributes().add(attribute);

            entity.setPrimaryKey(inheritedPrimaryKey);
        }

    }
    public static void mapInheritanceRelation(Entity entity, Class<?> supperClass) {
        Entity superClassEntity = EntitiesDataSource.getModelsSchemas().get(supperClass.getSimpleName());

        List<Attribute> inheritedAttrs = superClassEntity
                .getRelationalAtrributes()
                .stream()
                .filter(Attribute::isInherited)
                .collect(Collectors.toList());
        inheritedAttrs.forEach(a -> a.addHeritant(entity.getModel()));

        if (superClassEntity.getSupperClass() != Object.class)
            mapInheritanceRelation(entity, superClassEntity.getSupperClass());
    }

    public static void mapInheritanceNormalAttr(Entity entity, Class<?> supperClass) {
        Entity superClassEntity = EntitiesDataSource.getModelsSchemas().get(supperClass.getSimpleName());

        List<Attribute> inheritedAttrs = superClassEntity
                .getNormalAttributes()
                .stream()
                .filter(Attribute::isInherited)
                .collect(Collectors.toList());

        inheritedAttrs.forEach(a -> {
            Attribute clone;
            if (a instanceof AttributeList)
                clone = new AttributeList((AttributeList) a);
            else
                clone = new Attribute(a);

            entity.getNormalAttributes().stream().forEach(att -> {
                if(att.getOriginalName().equals(a.getOriginalName())){
                    try {
                        throw new ORMException("inherited field '"+ a.getOriginalName() +"' have the same name of existing field");
                    } catch (ORMException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            });

            clone.setName(a.getName());
            clone.setOriginalName(a.getOriginalName());
            clone.setClazz(entity.getModel());

            entity.addNormalInheritedAttr(clone);
        });

        if (superClassEntity.getSupperClass() != Object.class)
            mapInheritanceNormalAttr(entity,superClassEntity.getSupperClass());
    }

}

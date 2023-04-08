package org.easyorm.datamapper.utils;

import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.easyorm.modelsmapper.fieldsmapper.primarykey.PrimaryKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SettersInvoke {

    public static void setAttribute(Attribute attribute, Object object, Object newValue) {
        Method setterOfAttribute = null;
        try {
            setterOfAttribute = object.getClass()
                            .getMethod(
                                    "set" +
                                            attribute.getOriginalName().substring(0, 1)
                                                    .toUpperCase() +
                                            attribute.getOriginalName()
                                                    .substring(1),
                                    Class.forName("java." + (attribute.getType().equals("Date") ? "util." : "lang." ) + attribute.getType())
                            );
            try {
                setterOfAttribute.invoke(object,newValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setRelationalAttribute(Entity entity, Attribute attribute, Object object, Object newValue) {
        Method setterOfAttribute = null;
        try {
            setterOfAttribute = object.getClass()
                    .getMethod(
                            "set" +
                                    attribute.getOriginalName().substring(0, 1)
                                            .toUpperCase() +
                                    attribute.getOriginalName()
                                            .substring(1),
                            attribute instanceof AttributeList ? List.class : ( attribute.isInherited() ? entity.getSupperClass() : entity.getModel())
                    );
            try {
                setterOfAttribute.invoke(object,newValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setPrimaryKey(PrimaryKey primaryKey, Object object, Object newValue) {
        try {
            Method setterOfAttribute = object.getClass()
                    .getMethod(
                            "set" +
                                    primaryKey.getOriginalName().substring(0, 1)
                                            .toUpperCase() +
                                    primaryKey.getOriginalName()
                                            .substring(1),
                            Class.forName("java." + (primaryKey.getType().equals("Date") ? "util." : "lang." ) + primaryKey.getType())
                    );
            setterOfAttribute.invoke(object, newValue);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

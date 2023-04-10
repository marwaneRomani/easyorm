package org.easyorm.datamapper.utils;

import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.primarykey.PrimaryKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GettersInvoke {

    public static Object getAttributeValue(Attribute attribute, Object object) {
        try {
            Method getterOfAttribute = object.getClass()
                    .getMethod(
                            "get" +
                                    attribute.getOriginalName().substring(0, 1)
                                            .toUpperCase() +
                                    attribute.getOriginalName()
                                            .substring(1)
                    );
            Object value = getterOfAttribute.invoke(object);

            if (!attribute.isNullable() && value == null) {
                throw new Exception(attribute.getOriginalName() + " is required");
            }

            return value;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Object getPrimaryKeyValue(PrimaryKey primaryKey, Object object) {
        try {
            System.out.println(primaryKey.getOriginalName());
            System.out.println(object);
            Method getterOfAttribute = object.getClass()
                    .getMethod(
                            "get" +
                                    primaryKey.getOriginalName().substring(0, 1)
                                            .toUpperCase() +
                                    primaryKey.getOriginalName()
                                            .substring(1)
                    );
            Object value = getterOfAttribute.invoke(object);
            if (value == null) {
//                    throw new Exception("");
            }

            return value;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

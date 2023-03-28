package org.orm.framework.DataMapper1.Utils;

import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.PrimaryKey.PrimaryKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
                                    Class.forName("java.lang." + attribute.getType())
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

    public static void setPrimaryKey(PrimaryKey primaryKey, Object object, Object newValue) {
        try {
            Method setterOfAttribute = object.getClass()
                    .getMethod(
                            "set" +
                                    primaryKey.getOriginalName().substring(0, 1)
                                            .toUpperCase() +
                                    primaryKey.getOriginalName()
                                            .substring(1),
                            Class.forName("java.lang." + primaryKey.getType())
                    );
            setterOfAttribute.invoke(object, newValue);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

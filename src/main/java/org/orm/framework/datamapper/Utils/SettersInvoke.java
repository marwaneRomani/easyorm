package org.orm.framework.datamapper.Utils;

public class SettersInvoke {
//    public static Object setAttributeValue(Attribute attribute, Object object, Object newValue) {
//        try {
//            Method setterOfAttribute = object.getClass()
//                    .getMethod(
//                            "set" +
//                                    attribute.getOriginalName().substring(0, 1)
//                                            .toUpperCase() +
//                                    attribute.getOriginalName()
//                                            .substring(1),
//                            Class.forName("java.lang." + attribute.getType())
//                    );
//            setterOfAttribute.invoke(
//                    object, (((attribute.getClazz().getClass()) newValue)
//            );
//            Object value = getterOfAttribute.invoke(object);
//            System.out.println(getterOfAttribute.invoke(object) );
//            if (!attribute.isNullable() && value == null) {
//                throw new Exception(attribute.getOriginalName() + " is required");
//            }
//
//            return value;
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage() + " infound getter ");
//        }
//    }
}

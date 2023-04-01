package org.orm.framework.ModelsMapper.Mappers;

import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.Annotations.Column;
import org.orm.framework.ModelsMapper.Annotations.Id;
import org.orm.framework.ModelsMapper.Annotations.Ignore;
import org.orm.framework.ModelsMapper.Annotations.Table;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.AttributeList;
import org.orm.framework.ModelsMapper.FieldsMapper.PrimaryKey.PrimaryKey;
import org.orm.framework.customException.ORMException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapNormalAttributes {
    public static void mapModels(Entity entity) throws ORMException {

        List<Field> fields = getDeclaredFieldsOfModel(entity.getModel());
        PrimaryKey primaryKey = getPrimaryKey(fields);
        List<Field> modelFields = excludePrimaryKey(fields,primaryKey);

        Stream<Field> nonRelatioalFields = getNonRelatioalFieldsStream(entity.getModel(), modelFields);
        Stream<Field> relatioalFields = getRelatioalFieldsStream(entity.getModel(), modelFields);

        String name = setModelName(entity.getModel());
        Class<?> supperClass = entity.getModel().getSuperclass();
        List<Attribute> nonRelations = setModelAttributes(entity.getModel(),nonRelatioalFields).collect(Collectors.toList());
        List<Attribute> relations = setModelAttributes(entity.getModel(),relatioalFields).collect(Collectors.toList());

        entity.setName(name);
        entity.setSupperClass(supperClass);
        entity.setPrimaryKey(primaryKey);
        entity.setNormalAttributes(nonRelations);
        entity.setRelationalAtrributes(relations);
    }

    private static List<Field> getDeclaredFieldsOfModel(Class model) throws ORMException {
        if(model.getDeclaredFields().length==0){
            throw new ORMException("this model " + model.getSimpleName()+ " does not have any attributes");
        }
        return
                Arrays.stream(model.getDeclaredFields())
                        .filter(f -> !f.isAnnotationPresent(Ignore.class))
                        .collect(Collectors.toList());
    }

    private static Stream<Field> getNonRelatioalFieldsStream(Class<?> model,List<Field> fields) {

        return fields.stream().filter(f -> {
            Package fieldPackage = f.getType().getPackage();
            if (f.getType() == List.class) {
                ParameterizedType stringListType = (ParameterizedType) f.getGenericType();
                Class<?> genericListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                fieldPackage = genericListClass.getPackage();
            }

            return !fieldPackage.equals(model.getPackage());
        });
    }
    private static Stream<Field> getRelatioalFieldsStream(Class<?> model,List<Field> fields) {

        return fields.stream()
                .filter(f -> {
                    Package fieldPackage = f.getType().getPackage();
                    if (f.getType() == List.class) {
                        ParameterizedType stringListType = (ParameterizedType) f.getGenericType();
                        Class<?> genericListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                        fieldPackage = genericListClass.getPackage();
                    }
                    return fieldPackage.equals(model.getPackage());
                });
    }

    private static String setModelName(Class<?> model) {
        String name = model.getSimpleName();
        if (model.isAnnotationPresent(Table.class)) {
            Table table = model.getAnnotation(Table.class);
            if (! table.name().equals("")) {
                return table.name();
            }
        }
        return name;
    }

    private static PrimaryKey getPrimaryKey(List<Field> fields) {
        PrimaryKey primaryKey = new PrimaryKey();

        Field first = fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElse(null);

        if (first != null ) {
            Id id = first.getAnnotation(Id.class);
            if (!Objects.equals(id.name(), "")) {
                primaryKey.setName(id.name());
            }
            else
                primaryKey.setName(first.getName());
            primaryKey.setAutoIncrement(id.autoIncrement());
            primaryKey.setOriginalName(first.getName());//TODO
        }
        else {
            first = fields.stream()
                    .findFirst()
                    .get();
            primaryKey.setName(first.getName());
            primaryKey.setOriginalName(first.getName());
        }
        primaryKey.setType(first.getType().getSimpleName());
        primaryKey.setLength(lengthString(first));
        primaryKey.setDbType(getDbtype(first));

        return primaryKey;
    }

    private static List<Field> excludePrimaryKey(List<Field> modelFields, PrimaryKey primaryKey ) {
        return modelFields.stream()
                .filter(f -> !(f.getName().equals(primaryKey.getName()) ||
                        f.getName().equals(primaryKey.getOriginalName()))
                )
                .collect(Collectors.toList());
    }

    private static Stream<Attribute> setModelAttributes(Class<?> model,Stream<Field> modelFields) {
        return modelFields.map(f -> {
            if (f.getType() == List.class) {
                ParameterizedType stringListType = (ParameterizedType) f.getGenericType();
                Class<?> genericListClass = (Class<?>) stringListType.getActualTypeArguments()[0];

                AttributeList attributeList = new AttributeList();
                attributeList.setClazz(model);
                attributeList.setName(attributeName(f));
                attributeList.setOriginalName(f.getName());
                attributeList.setType(f.getType().getSimpleName());
                attributeList.setGenericType(genericListClass.getSimpleName());
                attributeList.setInherited(Modifier.isProtected(f.getModifiers()));
                attributeList.setNullable(nullable(f));
                attributeList.setUnique(unique(f));
                attributeList.setAutoIncrement(autoIncrement(f));
                attributeList.setDbType(getDbtype(f));

                return attributeList;
            }
            else {
                Attribute attribute = new Attribute();
                attribute.setClazz(model);
                attribute.setName(attributeName(f));
                attribute.setOriginalName(f.getName());
                attribute.setType(f.getType().getSimpleName());
                attribute.setInherited(Modifier.isProtected(f.getModifiers()));
                attribute.setNullable(nullable(f));
                attribute.setUnique(unique(f));
                attribute.setLength(lengthString(f));
                attribute.setAutoIncrement(autoIncrement(f));
                attribute.setDbType(getDbtype(f));

                return attribute;
            }
        });
    }

    private static String attributeName(Field f) {
        String name = f.getName();
        if (f.isAnnotationPresent(Column.class)) {
            Column column = f.getAnnotation(Column.class);
            if (!column.name().equals("")) {
                return column.name();
            }
        }
        return name;
    }

    private static Boolean nullable(Field f) {
        if (f.isAnnotationPresent(Column.class)) {
            Column column = f.getAnnotation(Column.class);
            return column.nullable();
        }
        return true;
    }

    private static Boolean unique(Field f) {
        if (f.isAnnotationPresent(Column.class)) {
            Column column = f.getAnnotation(Column.class);
            return column.unique();
        }
        return false;
    }

    private static Boolean autoIncrement(Field f) {
        if (f.isAnnotationPresent(Column.class)) {
            Column column = f.getAnnotation(Column.class);
            return column.autoIncrement();
        }
        return false;
    }

    /**
     *
     * @param f {Field}
     * @return 0 if not a string else is a string length
     */
    private static Integer lengthString(Field f) {
        if (f.getType().equals(String.class)) {
            if (f.isAnnotationPresent(Column.class)) {
                Column column = f.getAnnotation(Column.class);
                return column.length() == 0 ? 255 : column.length();
            }
            return 255;
        }
        return 0;
    }

    public static String getDbtype(Field f) {
        switch (f.getType().getSimpleName()) {
            case "Boolean":
                return "BOOL";
            case "Byte":
                return "TINYINT";
            case "Short":
                return "SMALLINT";
            case "Integer":
                return "INT";
            case "Long":
                return "BIGINT";
            case "Float":
                return "FLOAT";
            case "Double":
                return "DOUBLE";
            case "String":
                return "VARCHAR(100)";
            case "Date":
                return "DATE";
            }

            return "OTHER";
        }
}

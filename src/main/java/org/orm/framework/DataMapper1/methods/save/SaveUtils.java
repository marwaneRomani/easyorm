package org.orm.framework.DataMapper1.methods.save;

import org.orm.framework.DataMapper1.methods.Query;
import org.orm.framework.DataMapper.QueryBuilders.InsertQueryBuilder;
import org.orm.framework.DataMapper.QueryBuilders.UpdateQueryBuilder;
import org.orm.framework.DataMapper.Utils.FindAttributeRelation;
import org.orm.framework.DataMapper.Utils.GettersInvoke;
import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.AttributeList;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.ManyToMany;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.OneToMany;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.OneToOne;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.Relation;

import java.util.ArrayList;
import java.util.List;

public class SaveUtils<T> {

    public Query saveNormalAttrQuery(Entity entity, T objectToPersist) {
        InsertQueryBuilder insertQueryBuilder = new InsertQueryBuilder(entity.getName());

        if (!entity.getPrimaryKey().isAutoIncrement()) {
            insertQueryBuilder.column(
                    entity.getPrimaryKey().getName()
            ).value(
                    GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), objectToPersist)
            );
        }

        // specify the columns and values to insert
        entity.getNormalAttributes()
                .forEach(attribute -> {
                    if (!attribute.isAutoIncrement()) {
                        insertQueryBuilder.column(
                                attribute.getName()
                        ).value(
                                GettersInvoke.getAttributeValue(attribute, objectToPersist)
                        );
                    }
                });

        String sql = insertQueryBuilder.build();
        Object[] values = insertQueryBuilder.getValues();

        return new Query(sql, values);
    }

    public List<Query> saveRelation(Entity entity, Attribute attribute, T objectToPersist) {

        Relation relation = FindAttributeRelation.find(attribute, entity);

        if (relation instanceof OneToMany) {
            return saveOneToManyRelation(entity, relation, attribute, objectToPersist);
        }
        else if (relation instanceof OneToOne)
            return saveOneToOneRelation(entity, relation, attribute, objectToPersist);

        else {
            return saveManyToManyRelation(entity, relation, (AttributeList) attribute, objectToPersist);
        }
    }

    /**  ONE-TO-MANY RELATION --------------------------------------------------------------------------- */
    private List<Query> saveOneToManyRelation(Entity entity, Relation relation, Attribute attribute, Object objectToPersist) {
        if (relation.foreignKeyRef().equals(attribute.getName())) {
            return saveOneToManyRelationWithFk(entity, relation, attribute, objectToPersist);
        }
        else {
            return saveOneToManyRelationWithoutFk(entity, relation, attribute, objectToPersist);
        }
    }

    private List<Query> saveOneToManyRelationWithFk(Entity entity, Relation relation, Attribute attribute, Object object) {
            // create query
            UpdateQueryBuilder updateBuilder = new UpdateQueryBuilder();

            updateBuilder.setTableName(entity.getName().toLowerCase());


            Object objectRelation = GettersInvoke.getAttributeValue(attribute, object);


            // invoke getter of the primary key of the referenced object
            Object primaryKey = GettersInvoke.getPrimaryKeyValue
                    (
                            EntitiesDataSource
                                    .getModelsSchemas()
                                    .get(
                                            objectRelation
                                                    .getClass()
                                                    .getSimpleName()
                                    ).getPrimaryKey()
                            , objectRelation
                    );

            updateBuilder.set(relation.foreignKeyRef(), primaryKey);
            updateBuilder.where(
                    entity.getPrimaryKey().getName()  ,
                    GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(),object)
            );

            // execute query

            String sql = updateBuilder.build();
            Object[] values = updateBuilder.getValues();

            return List.of(new Query(sql, values));
        }

    private static List<Query> saveOneToManyRelationWithoutFk(Entity entity, Relation relation, Attribute attribute, Object object) {

        List<Query> queries = new ArrayList<>();

        // create query
        List<Object> objectsRelation = (List<Object>) GettersInvoke.getAttributeValue(attribute, object);

        // invoke getter of the primary key of the referenced object
        Object primaryKey = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object);

        objectsRelation
                .forEach(obj -> {
                    UpdateQueryBuilder updateBuilder = new UpdateQueryBuilder();

                    updateBuilder.setTableName(((AttributeList) attribute).getGenericType().toLowerCase());

                    updateBuilder.set(relation.foreignKeyRef(), primaryKey);

                    updateBuilder.where(
                            EntitiesDataSource
                                    .getModelsSchemas()
                                    .get(((AttributeList) attribute)
                                    .getGenericType()).getPrimaryKey().getName()

                                    ,
                                    GettersInvoke.getPrimaryKeyValue(EntitiesDataSource
                                                                    .getModelsSchemas()
                                                                    .get(((AttributeList) attribute)
                                                                    .getGenericType()).getPrimaryKey(), obj));


                    String sql = updateBuilder.build();


                    Object[] values = updateBuilder.getValues();
                    queries.add(new Query(sql, values));
                });

        return queries;
    }

    /** ONE-TO-ONE RELATION -----------------------------------------------------------------------------------------------*/
    private List<Query> saveOneToOneRelation(Entity entity, Relation relation, Attribute attribute, Object object) {
        if (relation.foreignKeyRef().equals(attribute.getName())) {
            return saveOneToOneRelationWithFk(entity, relation, attribute, object);
        }
        else {
            return saveOneToOneRelationWithoutFk(entity, relation, attribute, object);
        }

    }

    private List<Query> saveOneToOneRelationWithFk(Entity entity, Relation relation, Attribute attribute, Object object) {

            // create query
            UpdateQueryBuilder updateBuilder = new UpdateQueryBuilder();

            updateBuilder.setTableName(entity.getName().toLowerCase());


            Object objectRelation = GettersInvoke.getAttributeValue(attribute, object);

            // invoke getter of the primary key of the referenced object
            Object primaryKey = GettersInvoke.getPrimaryKeyValue(
                            EntitiesDataSource
                                    .getModelsSchemas()
                                    .get(
                                            objectRelation
                                                    .getClass()
                                                    .getSimpleName()
                                    ).getPrimaryKey()
                            , objectRelation
                    );

            updateBuilder.set(relation.foreignKeyRef(), primaryKey);
            updateBuilder.where(entity.getPrimaryKey().getName()  , GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(),object));

            // execute query

            String sql = updateBuilder.build();
            Object[] values = updateBuilder.getValues();


            return List.of(new Query(sql, values));
    }

    private List<Query> saveOneToOneRelationWithoutFk(Entity entity, Relation relation, Attribute attribute, Object object) {
            // create query

            Object objectRelation = GettersInvoke.getAttributeValue(attribute, object);

            // invoke getter of the primary key of the referenced object

            Object primaryKey = GettersInvoke.getPrimaryKeyValue
                    (
                            entity.getPrimaryKey()
                            , object
                    );

            UpdateQueryBuilder updateBuilder = new UpdateQueryBuilder();

            updateBuilder.setTableName(attribute.getType().toLowerCase());

            updateBuilder.set(relation.foreignKeyRef(), primaryKey);

            updateBuilder.where(
                    entity.getPrimaryKey().getName() ,
                            GettersInvoke.getPrimaryKeyValue(
                                    EntitiesDataSource
                                            .getModelsSchemas()
                                            .get(attribute
                                                    .getType()).getPrimaryKey()
                                    ,objectRelation
                            )
            );

            // execute query
            String sql = updateBuilder.build();
            Object[] values = updateBuilder.getValues();

            return List.of(new Query(sql,values));
    }

    /** MANY-TO-MANY RELATION -------------------------------------------------------------------------- */

    private List<Query> saveManyToManyRelation(Entity entity, Relation relation, AttributeList attribute, Object object) {
            List<Query> queries = new ArrayList<>();
            List<Object> attributesValue = (List<Object>) GettersInvoke.getAttributeValue(attribute, object);

            attributesValue
                    .forEach(obj -> {

                        InsertQueryBuilder queryBuilder = new InsertQueryBuilder(((ManyToMany) relation).getTableName());

                        Object primaryKey = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object);

                        queryBuilder.column(entity.getName() + '_' + entity.getPrimaryKey().getName()).value(primaryKey);

                        queryBuilder
                                .column(EntitiesDataSource.getModelsSchemas().get(attribute.getGenericType()).getName() + '_' + EntitiesDataSource.getModelsSchemas().get(attribute.getGenericType()).getPrimaryKey().getName())
                                .value(GettersInvoke.getPrimaryKeyValue(EntitiesDataSource.getModelsSchemas().get(attribute.getGenericType()).getPrimaryKey(), obj));

                        String sql = queryBuilder.build();
                        Object[] values = queryBuilder.getValues();
                        queries.add(new Query(sql, values));
                    });

        return queries;
    }


}

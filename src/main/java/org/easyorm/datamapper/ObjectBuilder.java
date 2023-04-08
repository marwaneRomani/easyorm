package org.easyorm.datamapper;


import org.easyorm.datamapper.methods.delete.Delete;
import org.easyorm.applicationstate.ApplicationState;
import org.easyorm.connectionpool.ConnectionPool;
import org.easyorm.datamapper.jdbctemplate.JdbcTemplateImpl;
import org.easyorm.datamapper.methods.update.Update;
import org.easyorm.datamapper.utils.FindAttribute;
import org.easyorm.datamapper.utils.FindAttributeRelation;
import org.easyorm.datamapper.utils.GettersInvoke;
import org.easyorm.datamapper.utils.SettersInvoke;
import org.easyorm.datamapper.methods.find.Find;
import org.easyorm.datamapper.methods.save.Save;
import org.easyorm.entitiesdtasource.EntitiesDataSource;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.easyorm.modelsmapper.fieldsmapper.relation.OneToMany;
import org.easyorm.modelsmapper.fieldsmapper.relation.Relation;
import org.easyorm.EasyOrm;
import org.easyorm.transactionsmanager.Transaction;
import org.easyorm.customexception.ORMException;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder<T> {
    private final Class<?> model;
    private final Boolean transactionalCode;
    private final Entity entity;
    private final ApplicationState state;
    private boolean isFindOneMethod;
    private T object;
    private List<T> objects;

    public ObjectBuilder(Class<?> model) {
        this.model = model;
        this.state = ApplicationState.getState();
        try {
            object = (T) Class.forName(model.getName()).newInstance();
            entity = EntitiesDataSource.getModelsSchemas().get(model.getSimpleName());
            this.transactionalCode = true;
            if (entity == null) {
                throw new Exception("entity not found.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ObjectBuilder(Class<?> model, Boolean transactional) {
        this.model = model;
        this.state = ApplicationState.getState();
        try {
            object = (T) Class.forName(model.getName()).newInstance();
            entity = EntitiesDataSource.getModelsSchemas().get(model.getSimpleName());
            this.transactionalCode = transactional;

            if (entity == null) {
                throw new Exception("entity not found.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    /**
     * persistence methods
     *
     **/

    public T save(T object) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Save<T> saveObject = new Save<>(new JdbcTemplateImpl(connection));


            Method saveMethod = Save.class.getMethod("save", Entity.class, Object.class);

            if (transactionalCode)
                Transaction.wrapMethodInTransaction(connection, saveObject, saveMethod, entity, object);
            else
                saveMethod.invoke(saveObject, entity, object);

            pool.releaseConnection(connection);

            return object;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectBuilder<T> findAll() {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));

            this.objects = findObject.findAll(entity);
//            this.object = null;

            pool.releaseConnection(connection);

            return this;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectBuilder<T> findById(Object id) {
        try {
            isFindOneMethod = true;
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));

            this.object = findObject.findById(entity, id);
//            this.objects = null;

            pool.releaseConnection(connection);

            return this;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public SearchQueryBuilder findOne() {
        isFindOneMethod = true;
        List<String> keys = new ArrayList<>();
        List<String> conditionTypes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<String> chain = new ArrayList<>(); // and || or

        SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder(this, "one" , keys, conditionTypes, values, chain, 0);

        return searchQueryBuilder;
    }

    public SearchQueryBuilder findMany() {
        isFindOneMethod = false;
        List<String> keys = new ArrayList<>();
        List<String> conditionTypes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<String> chain = new ArrayList<>();
        Integer limit = new Integer(0);
        SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder(this, "many" , keys, conditionTypes, values, chain, limit);

        return searchQueryBuilder;
    }


    private void executeFindOne(List<String> keys, List<String> conditionTypes, List<Object> values, List<String> chain) throws ORMException, SQLException {
        if (keys == null || conditionTypes == null || values == null || chain == null) {
            throw new ORMException("All search parameters are required and cannot be null.");
        }

        ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
        Connection connection = pool.getConnection();

        Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));

        this.object = findObject.findOne(entity, keys, conditionTypes ,values, chain);

        pool.releaseConnection(connection);
    }


    private void executeFindMany(List<String> keys, List<String> conditionTypes, List<Object> values, List<String> chain, Integer limit) throws ORMException, SQLException {
        if (keys == null || conditionTypes == null || values == null || chain == null) {
            throw new ORMException("All search parameters are required and cannot be null.");
        }

        ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
        Connection connection = pool.getConnection();

        Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));

        this.objects = findObject.findMany(entity, keys, conditionTypes ,values, chain, limit);

        pool.releaseConnection(connection);
    }



    public ObjectBuilder<T> get(String relationName) {
        if (object != null || objects != null) {
            try {
                ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
                Connection connection = pool.getConnection();

                Attribute attribute = FindAttribute.find(entity, relationName);
                Relation relation = FindAttributeRelation.find(attribute, entity);

                Entity attributeEntity = EntitiesDataSource
                        .getModelsSchemas()
                        .get((attribute instanceof AttributeList) ? ((AttributeList) attribute).getGenericType() : attribute.getType() );

                Class<?> attributeClass = attributeEntity.getModel();

                Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));


                if (attribute instanceof AttributeList) {
                    if (relation instanceof OneToMany) {
                        if(!isFindOneMethod) {
                            objects.forEach( object_ -> {
                                // query exaple:  Select cin, name, age from `user`  WHERE  user.filiere = "GL";
                                System.out.println(object_ + "9bl");
                                Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object_);

                                List<?> foundObjects = EasyOrm
                                        .buildObject(attributeClass)
                                        .findMany()
                                        .where(relation.getOne().getName(), "=", primaryKeyValue)
                                        .execute()
                                        .buildObjects();

                                SettersInvoke.setRelationalAttribute(attributeEntity, attribute, object_, foundObjects);
                            });
                        } else {
                            if (!isFindOneMethod) {
                                objects.forEach(object_ -> {
                                    // query exaple:  Select cin, name, age from `user`  WHERE  user.filiere = "GL";
                                    System.out.println(object_ + "9bl");
                                    Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object_);

                                    List<?> foundObjects = EasyOrm
                                            .buildObject(attributeClass)
                                            .findMany()
                                            .where(relation.getOne().getName(), "=", primaryKeyValue)
                                            .execute()
                                            .buildObjects();

                                    SettersInvoke.setRelationalAttribute(attributeEntity, attribute, object_, foundObjects);
                                });
                            } else {
                                // query exaple:  Select cin, name, age from `user`  WHERE  user.filiere = "GL";
                                System.out.println(object + "9bl");
                                Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object);

                                List<?> foundObjects = EasyOrm
                                        .buildObject(attributeClass)
                                        .findMany()
                                        .where(relation.getOne().getName(), "=", primaryKeyValue)
                                        .execute()
                                        .buildObjects();

                                SettersInvoke.setRelationalAttribute(attributeEntity, attribute, object, foundObjects);
                            }

                        }

                    }
                    else {
                        // ManyToMany
                        // Query Example :
                        //  SELECT produit.id, produit.nom, produit.prix
                        //  FROM commande_produit, commande, produit
                        //  WHERE commande.id = commande_produit.commande_id
                        //        AND produit.id = commande_produit.produit_id
                        //        AND commande.id = 1
                        if (!isFindOneMethod) {
                            objects.forEach(object_ -> {
                                Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object_);
                                List<Object> fromManyToManyRelation = findObject.findFromManyToManyRelation(attributeEntity, entity, relation, primaryKeyValue);

                                SettersInvoke.setRelationalAttribute(attributeEntity, attribute, object_, fromManyToManyRelation);
                            });
                        } else {
                            Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object);
                            List<Object> fromManyToManyRelation = findObject.findFromManyToManyRelation(attributeEntity, entity, relation, primaryKeyValue);
                            System.out.println(fromManyToManyRelation);

                            SettersInvoke.setRelationalAttribute(attributeEntity, attribute, object, fromManyToManyRelation);

                        }

                    }
                }
                else {
                    if (relation instanceof OneToMany) {

                        if (!isFindOneMethod) {
                            objects.forEach(object_ -> {
                                //Query example Select * from filiere WHERE  filiere.nom = (SELECT filiere  from `user` u WHERE  u.cin = "123456789")
                                Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object_);

                                Object relationObject = findObject.findForeignKey(entity, primaryKeyValue, attributeEntity, attribute.getName());
                                System.out.println(relationObject);

                                SettersInvoke.setRelationalAttribute(attributeEntity, attribute, object_, relationObject);
                            });
                        } else {
                            //Query example Select * from filiere WHERE  filiere.nom = (SELECT filiere  from `user` u WHERE  u.cin = "123456789")

                            Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object);

                            Object relationObject = findObject.findForeignKey(entity, primaryKeyValue, attributeEntity, attribute.getName());
                            System.out.println(relationObject);

                            SettersInvoke.setRelationalAttribute(attributeEntity, attribute, object, relationObject);
                        }

                    }
                    else  {
                        if (!isFindOneMethod) {
                            objects.forEach(object_ -> {
                                // Query example : Select * from `user`  WHERE  user.filiere = "GL";
                                Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object);

                                Object o = EasyOrm
                                        .buildObject(attributeClass)
                                        .findOne()
                                        .where(relation.getOne().getName(), "=", primaryKeyValue)
                                        .execute()
                                        .buildObject();

                                System.out.println(o);
                            });
                        } else {
                            // Query example : Select * from `user`  WHERE  user.filiere = "GL";
                            Object primaryKeyValue = GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object);

                            Object o = EasyOrm
                                    .buildObject(attributeClass)
                                    .findOne()
                                    .where(relation.getOne().getName(), "=", primaryKeyValue)
                                    .execute()
                                    .buildObject();

                            System.out.println(o);
                        }

                    }
                }

                pool.releaseConnection(connection);

                return this;
            } catch (ORMException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public T deleteById(Object id) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Delete<T> deleteObject = new Delete<>(new JdbcTemplateImpl(connection));

            T deletedObject = deleteObject.deleteById(entity, id);

            pool.releaseConnection(connection);

            return deletedObject;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T updateById(Object id) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Update<T> updateObject = new Update<>(new JdbcTemplateImpl(connection));

            pool.releaseConnection(connection);

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public T buildObject() {
        return object;
    }

    public List<T> buildObjects() {
        return objects;
    }


    public class SearchQueryBuilder {
        private ObjectBuilder<T> builder;
        private String manyOrOne;
        private List<String> keys;
        private List<String> conditionTypes;
        private List<Object> values;
        private List<String> chain;
        private Integer limit;

        public SearchQueryBuilder(ObjectBuilder<T> builder, String manyOrOne ,List<String> keys, List<String> conditionTypes, List<Object> values, List<String> chain, Integer limit) {
            this.builder = builder;
            this.manyOrOne = manyOrOne;
            this.keys = keys;
            this.conditionTypes = conditionTypes;
            this.values = values;
            this.chain = chain;
            this.limit = limit;
        }

        public SearchQueryBuilder where(String field, String operator, Object value) {
            try {
                if (field == null || operator == null || value == null )
                    throw new ORMException("params should not be null");

                if (entity.getPrimaryKey().getName().equals(field)) {
                    keys.add(field);
                    conditionTypes.add(operator);
                    values.add(value);
                }
                else {
                    Attribute attribute = entity
                            .getNormalAttributes()
                            .stream()
                            .filter(att -> att.getName().equals(field)).findFirst().orElse(null);

                    if (attribute == null) {
                        Attribute foreignKey = entity
                                .getForeignKeys()
                                .stream()
                                .filter(att -> att.getName().equals(field)).findFirst().orElse(null);

                        if (foreignKey == null)
                            throw new ORMException("No attribute with the name " + field + " in this class !");

                        keys.add(field);
                        conditionTypes.add(operator);
                        values.add(value);
                        return this;
                    }


                    keys.add(field);
                    conditionTypes.add(operator);
                    values.add(value);
                }

                return this;
            }
            catch (ORMException e) {
                throw new RuntimeException(e);
            }
        }
        public SearchQueryBuilder and(String field, String operator, Object value) {
            int keysSize = keys.size();
            this.where(field, operator, value);

            if (keysSize != 0)
                chain.add("and");

            return this;
        }

        public SearchQueryBuilder or(String field, String operator, Object value) {
            int keysSize = keys.size();
            this.where(field, operator, value);

            if (keysSize != 0)
                chain.add("or");

            return this;
        }

        public SearchQueryBuilder limit(Integer value) {
            //TODO if value is null throw an exception
            limit = value;
            return this;
        }

        public ObjectBuilder<T> execute() {
            try {
                if (manyOrOne.equals("one"))
                    builder.executeFindOne(keys, conditionTypes, values, chain);
                else
                    builder.executeFindMany(keys, conditionTypes, values, chain, limit);
                return builder;

            } catch (ORMException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


}

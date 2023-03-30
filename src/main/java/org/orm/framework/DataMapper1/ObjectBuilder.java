package org.orm.framework.DataMapper1;

import org.orm.framework.ApplicationState.ApplicationState;
import org.orm.framework.ConnectionsPool.ConnectionPool;
import org.orm.framework.DataMapper1.JdbcTemplate.JdbcTemplateImpl;
import org.orm.framework.DataMapper1.Utils.FindAttribute;
import org.orm.framework.DataMapper1.Utils.FindAttributeRelation;
import org.orm.framework.DataMapper1.methods.find.Find;
import org.orm.framework.DataMapper1.methods.save.Save;
import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.AttributeList;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.OneToMany;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.Relation;
import org.orm.framework.OrmApplication;
import org.orm.framework.TransactionsManager.Transaction;
import org.orm.framework.customException.ORMException;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ObjectBuilder<T> {
    private final Class<?> model;
    private final Entity entity;
    private final ApplicationState state;

    private T object;
    private List<T> objects;

    public ObjectBuilder(Class<?> model) {
        this.model = model;
        this.state = ApplicationState.getState();
        try {
            object = (T) Class.forName(model.getName()).newInstance();
            entity = EntitiesDataSource.getModelsSchemas().get(model.getSimpleName());

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

            Transaction.wrapMethodInTransaction(connection, saveObject, saveMethod, entity, object);

            pool.releaseConnection(connection);

            return object;

        } catch (SQLException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectBuilder<T> findAll() {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));

            this.objects = findObject.findAll(entity);

            pool.releaseConnection(connection);

            return this;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectBuilder<T> findById(Object id) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));

            object = findObject.findById(entity, id);

            pool.releaseConnection(connection);

            return this;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public SearchQueryBuilder findOne() {
        List<String> keys = new ArrayList<>();
        List<String> conditionTypes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<String> chain = new ArrayList<>(); // and || or

        SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder(this, "one" , keys, conditionTypes, values, chain);

        return searchQueryBuilder;
    }

    public SearchQueryBuilder findMany() {
        List<String> keys = new ArrayList<>();
        List<String> conditionTypes = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        List<String> chain = new ArrayList<>();

        SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder(this, "many" , keys, conditionTypes, values, chain);

        return searchQueryBuilder;
    }


    private void executeFindOne(List<String> keys, List<String> conditionTypes, List<Object> values, List<String> chain) throws ORMException, SQLException {
        if (keys == null || conditionTypes == null || values == null || chain == null) {
            throw new ORMException("All search parameters are required and cannot be null.");
        }

        ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
        Connection connection = pool.getConnection();

        Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));

        object = findObject.findOne(entity, keys, conditionTypes ,values, chain);

        pool.releaseConnection(connection);
    }


    private void executeFindMany(List<String> keys, List<String> conditionTypes, List<Object> values, List<String> chain) throws ORMException, SQLException {
        if (keys == null || conditionTypes == null || values == null || chain == null) {
            throw new ORMException("All search parameters are required and cannot be null.");
        }

        ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
        Connection connection = pool.getConnection();

        Find<T> findObject = new Find<>(new JdbcTemplateImpl(connection));

        objects = findObject.findMany(entity, keys, conditionTypes ,values, chain);

        pool.releaseConnection(connection);
    }



    public ObjectBuilder<T> get(String relationName) {
        try {
            ConnectionPool pool = ConnectionPool.getInstance(state.getUrl(),state.getUsername(),state.getPassword(), state.getConnectionPoolMaxSize());
            Connection connection = pool.getConnection();

            Attribute attribute = FindAttribute.find(entity, relationName);
            Relation relation = FindAttributeRelation.find(attribute, entity);

            if (attribute instanceof AttributeList) {
                if (relation instanceof OneToMany) {

                }
                else  {

                }
            }
            else {
                if (relation instanceof OneToMany) {
                    OrmApplication
                            .buildObject(attribute.getClazz())
                            .findOne();
                }
                else  {

                }
            }

            pool.releaseConnection(connection);

            return this;
        } catch (ORMException | SQLException e) {
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


        public SearchQueryBuilder(ObjectBuilder<T> builder, String manyOrOne ,List<String> keys, List<String> conditionTypes, List<Object> values, List<String> chain) {
            this.builder = builder;
            this.manyOrOne = manyOrOne;
            this.keys = keys;
            this.conditionTypes = conditionTypes;
            this.values = values;
            this.chain = chain;
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

                    if (attribute == null)
                        throw new ORMException("No attribute with the name " + field + " in this class !");

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

        public ObjectBuilder<T> execute() {
            try {
                if (manyOrOne.equals("one"))
                    builder.executeFindOne(keys, conditionTypes, values, chain);
                else
                    builder.executeFindMany(keys, conditionTypes, values, chain);
                return builder;

            } catch (ORMException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


}

package org.orm.framework.DataMapper.ObjectBuilders;

import org.orm.framework.DataMapper.ObjectBuilders.find.FindBuilder;
import org.orm.framework.DataMapper.ObjectBuilders.save.SaveBuilder;
import org.orm.framework.DataMapper.Utils.FindAttribute;
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
import java.util.function.Predicate;

public class ObjectBuilder<T> {
    private Class<?> model;
    private T object;
    private Entity entity;


    public ObjectBuilder(Class<?> model) {
        this.model = model;
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
    * */

    public T save(T object) {
        SaveBuilder<T> saveBuilder = new SaveBuilder<>(entity);

        // build queries
        List<Query> queries = saveBuilder.save(object);

        this.build(queries);

        return object;
    }

    public List<T> findAll() {
        FindBuilder findBuilder = new FindBuilder(entity);

        Query query = findBuilder.findAll();

        return null;
    }


    public ObjectBuilder<T> findById(Object id) {
        FindBuilder findBuilder = new FindBuilder(entity);
        Query byId = findBuilder.findById(id);

        System.out.println(byId.getQuery());
        // execute the query

        return this;
    }


    public ObjectBuilder<T> findOne(Predicate<T> predicate) {
        return this;
    }


    public ObjectBuilder<T> get(String relationName) {
        FindBuilder findBuilder = new FindBuilder(entity);

        Attribute attribute = FindAttribute.find(entity, relationName);
        Relation relation = FindAttributeRelation.find(attribute, entity);


        if (attribute instanceof AttributeList) {
            if (relation instanceof OneToMany) {
                FindBuilder findBuilder1 = new FindBuilder(EntitiesDataSource.getModelsSchemas().get(((AttributeList) attribute).getGenericType()));
                Query queryForList = findBuilder1.findByCondition(relation.foreignKeyRef(), GettersInvoke.getPrimaryKeyValue(entity.getPrimaryKey(), object));
                System.out.println("execute " + queryForList.getQuery());
                System.out.println("values " + queryForList.getValues());
                // set the result to the object
            }
            else {
                // select * from Player p , Player_Match p_m where p.id = p_m.idp;
                FindBuilder findBuilder1 = new FindBuilder(EntitiesDataSource.getModelsSchemas().get(((AttributeList) attribute).getGenericType()));
                findBuilder1
                     .findFromManyTables(((ManyToMany)relation).getTableName(), null);

            }
        }
        else {

        }

        return this;
    }

    public T build(List<Query> queries) {
        return object;
    }
}

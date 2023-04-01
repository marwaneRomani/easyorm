package org.orm.framework.DataMapper1.methods.find;

import org.orm.framework.DataMapper.QueryBuilders.SelectQueryBuilder;
import org.orm.framework.DataMapper1.methods.Query;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.ManyToMany;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.Relation;

import java.util.List;

public class FindUtils<T> {

    public Query findAll(Entity entity) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());

        selectQueryBuilder.addColumn(entity.getPrimaryKey().getName());

        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));

        Query query = selectQueryBuilder.build();

        return query;
    }


    public Query findById(Entity entity, Object id) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        // Set the source table
        selectQueryBuilder.setTable(entity.getName());

        // select primary key
        selectQueryBuilder.addColumn(entity.getPrimaryKey().getName());

        // select normal attributes
        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));
        selectQueryBuilder.addEqualCondition(entity.getPrimaryKey().getName(), id);

        Query query = selectQueryBuilder.build();

        return query;
    }

    public Query find(Entity entity, List<String> keys, List<String> conditionTypes , List<Object> values, List<String> chains, Integer limit) {

        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());

        // select primary key
        selectQueryBuilder.addColumn(entity.getPrimaryKey().getName());

        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));
        if (limit != 0) {
            selectQueryBuilder.setLimit(limit);
        }
        for (int i = 0; i < keys.size(); i++) {
            String condition = conditionTypes.get(i);

            String chainingOperator = null;


            if (i < keys.size() - 1) {
                chainingOperator= chains.get(i);
            }

            if (condition.equalsIgnoreCase("equals") || condition.equalsIgnoreCase("=") ) {
                selectQueryBuilder.addEqualCondition(keys.get(i), values.get(i));

                if (chainingOperator != null)
                    selectQueryBuilder.addChainOperation(chainingOperator);
            }
            else if (condition.toLowerCase().equals("like")) {
                selectQueryBuilder.addLikeCondition(keys.get(i), values.get(i));

                if (chainingOperator != null)
                    selectQueryBuilder.addChainOperation(chainingOperator);

            }
            else if (condition.equalsIgnoreCase("different") || condition.equalsIgnoreCase("<>") || condition.equalsIgnoreCase("not equals")) {
                selectQueryBuilder.addNotEqualCondition(keys.get(i), values.get(i));
                if (chainingOperator != null)
                    selectQueryBuilder.addChainOperation(chainingOperator);
            }

            else if (condition.equalsIgnoreCase("greater then") || condition.equalsIgnoreCase(">") ) {
                selectQueryBuilder.addGreaterThenCondition(keys.get(i), values.get(i));
                if (chainingOperator != null)
                    selectQueryBuilder.addChainOperation(chainingOperator);
            }
            else if (condition.equalsIgnoreCase("less then") || condition.equalsIgnoreCase("<")) {
                selectQueryBuilder.addLessThenCondition(keys.get(i), values.get(i));

                if (chainingOperator != null)
                    selectQueryBuilder.addChainOperation(chainingOperator);
            }
        }


        Query query = selectQueryBuilder.build();

        return query;
    }

    public Query findForeignKey(Entity entity, Object primaryKeyValue, String foreignKeyName) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());

        // select primary key
        selectQueryBuilder.addColumn(foreignKeyName);

        // add condtions
        selectQueryBuilder.addEqualCondition(entity.getPrimaryKey().getName(), primaryKeyValue);

        Query query = selectQueryBuilder.build();

        return query;
    }

    public Query findFromManyToManyRelation(Entity searchedEntity, Entity entity, Relation relation, Object entityPrimaryKeyValue) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        // set tables :
        selectQueryBuilder.setTable(searchedEntity.getName());
        selectQueryBuilder.setTable(entity.getName());
        selectQueryBuilder.setTable(((ManyToMany)relation).getTableName().toLowerCase());

        // set returning values
        selectQueryBuilder.addColumn(searchedEntity.getName() + '.' + searchedEntity.getPrimaryKey().getName());
        searchedEntity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(searchedEntity.getName() + '.' + attribute.getName()));

        // set conditions
        selectQueryBuilder.addEqualCondition(((ManyToMany) relation).getTableName() + '.' + entity.getName().toLowerCase() + "_" + entity.getPrimaryKey().getName(), entity.getName() + '.' + entity.getPrimaryKey().getName());
        selectQueryBuilder.addEqualCondition(((ManyToMany) relation).getTableName() + '.' + searchedEntity.getName().toLowerCase() + "_" +  searchedEntity.getPrimaryKey().getName(), searchedEntity.getName() +  '.' + searchedEntity.getPrimaryKey().getName());
        selectQueryBuilder.addEqualCondition(entity.getName() + '.' + entity.getPrimaryKey().getName(), entityPrimaryKeyValue);

        //  SELECT produit.id, produit.nom, produit.prix
        //  FROM commande_produit, commande, produit
        //  WHERE commande_produit.commande_id = commande.id
        //        AND commande_produit.produit_id = produit.id
        //        AND commande.id = 1

        selectQueryBuilder.addChainOperation(" AND ");
        selectQueryBuilder.addChainOperation(" AND ");

        Query query = selectQueryBuilder.build();

        return query;
    }
//    SELECT user.cin,user.cne,user.name,user.email,user.lastName,user.age
//    FROM user, filiere, filiere_user
//    WHERE filiere_user.manyFilieres = ?  AND
//          filiere_user.manyUsers = ?  AND
//          filiere.nom = ?

}

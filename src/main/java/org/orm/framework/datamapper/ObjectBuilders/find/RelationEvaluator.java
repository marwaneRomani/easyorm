package org.orm.framework.datamapper.ObjectBuilders.find;

import org.orm.framework.datamapper.Utils.FindAttribute;
import org.orm.framework.datamapper.Utils.FindAttributeRelation;
import org.orm.framework.entitiesdtasource.EntitiesDataSource;
import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.orm.framework.modelsmapper.fieldsmapper.relation.OneToMany;
import org.orm.framework.modelsmapper.fieldsmapper.relation.OneToOne;
import org.orm.framework.modelsmapper.fieldsmapper.relation.Relation;

public class RelationEvaluator<T> {
    private Attribute attribute ;
    private Relation relation;

    private Entity entity;
    private Entity relationEntity;

    private T object;

    public RelationEvaluator(Entity entity, String relationName, T object) {
        this.attribute = FindAttribute.find(entity, relationName);
        this.relation = FindAttributeRelation.find(attribute, entity);
        this.object = object;

        this.entity = entity;

        if (attribute instanceof AttributeList)
            this.relationEntity = EntitiesDataSource.getModelsSchemas()
                    .get(((AttributeList) attribute).getGenericType());
        else
            this.relationEntity = EntitiesDataSource.getModelsSchemas()
                    .get(attribute.getType());
    }
    public void evaluate() {
        if (relation instanceof OneToOne) {
            EvaluateOneToOne oneToOneEval = new EvaluateOneToOne();

            if (relation.foreignKeyRef().equals(attribute.getName())) {
                oneToOneEval.evaluateRelationWithFk();
            }
            else {
                oneToOneEval.evaluateRelationWithoutFk();
            }
        }
        else if (relation instanceof OneToMany) {
            EvaluateOneToMany oneToManyEval = new EvaluateOneToMany();

            if (relation.foreignKeyRef().equals(attribute.getName())) {
                oneToManyEval.evaluateRelationWithFk();
            }
            else {
                oneToManyEval.evaluateRelationWithoutFk();
            }
        }
        else {
            EvaluateManyToMany manyToManyEval = new EvaluateManyToMany();
            manyToManyEval.evaluateManyToMany();
        }
    }

    private class EvaluateOneToOne {
        private void evaluateRelationWithFk() {
            // 2 requetes
            // query1 = select fk from entity.getName() where entity.getPrimaryKey().getName() = GettersInvoke.invokePk(object);
            // result = execute query1
            // SingleObj = select * from relationEntity.getName() where relationEntity.getPK().getName() = result;
            // SetterInvoke( attribute ,object, SingleObj)
        }

        private void evaluateRelationWithoutFk() {
            // query = select * from relationEntity.getName() where relation.fk() = GettersInvoke.invokePk(object)
            // SingleResult = execute(query);
            // SetterInvoke( attribute ,object, singleResult);
        }
    }

    private class EvaluateOneToMany {
        private void evaluateRelationWithFk() {
            // 2 requetes
            // query1 = select fk from entity.getName() where entity.getPrimaryKey().getName() = GettersInvoke.invokePk(object);
            // result = execute query1
            // SingleObj = select * from relationEntity.getName() where relationEntity.getPK().getName() = result;
            // SetterInvoke( attribute ,object, SingleObj)
        }

        private void evaluateRelationWithoutFk() {
            // query = select * from relationEntity.getName() where relation.fk() = GettersInvoke.invokePk(object)
            // ListResult = execute(query);
            // SetterInvoke( attribute ,object, singleResult);
        }
    }

    private class EvaluateManyToMany {
        private void evaluateManyToMany() {
            // SELECT *
            // FROM match, team_match WHERE team_match.getterPK(team) = team_match AND
            //             team_match.pkMatch = match.pk;
        }
    }
}















package org.orm.framework.DataMapper1.methods.find;

import org.orm.framework.DataMapper.QueryBuilders.SelectQueryBuilder;
import org.orm.framework.DataMapper1.methods.Query;
import org.orm.framework.EntitiesDataSource.Entity;

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

        public Query find(Entity entity, List<String> keys,List<String> conditionTypes ,List<Object> values) {
        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder();

        selectQueryBuilder.setTable(entity.getName());

        // select primary key
        selectQueryBuilder.addColumn(entity.getPrimaryKey().getName());

        entity.getNormalAttributes().forEach(attribute -> selectQueryBuilder.addColumn(attribute.getName()));

        for (int i = 0; i < keys.size(); i++) {

            if (keys.get(i).equalsIgnoreCase("limit")) {
                selectQueryBuilder.setLimit((Integer) values.get(i));
            }
            else {

                String condition = conditionTypes.get(i);
                if (condition.equalsIgnoreCase("equals") || condition.equalsIgnoreCase("=") || condition.toLowerCase().equals("like"))
                    selectQueryBuilder.addEqualCondition(keys.get(i), values.get(i));

                else if (condition.equalsIgnoreCase("different") || condition.equalsIgnoreCase("<>") || condition.equalsIgnoreCase("not equals"))
                    selectQueryBuilder.addNotEqualCondition(keys.get(i), values.get(i));

                else if (condition.equalsIgnoreCase("greater then") || condition.equalsIgnoreCase(">") )
                    selectQueryBuilder.addGreaterThenCondition(keys.get(i), values.get(i));

                else if (condition.equalsIgnoreCase("less then") || condition.equalsIgnoreCase("<"))
                    selectQueryBuilder.addLessThenCondition(keys.get(i), values.get(i));

            }
        }


        Query query = selectQueryBuilder.build();

        return query;
    }


}

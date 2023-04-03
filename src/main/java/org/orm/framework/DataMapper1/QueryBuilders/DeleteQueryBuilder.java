package org.orm.framework.DataMapper1.QueryBuilders;

import org.orm.framework.DataMapper1.methods.Query;

import java.util.ArrayList;
import java.util.List;

public class DeleteQueryBuilder {
    private String tableName;
    private List<String> conditions = new ArrayList<>();
    private List<ConditionType> conditionsType = new ArrayList<>();

    private Boolean chainConditionsFLag = false;


    private enum ConditionType {
        EQUAL,
        LIKE,
        NOTEQUAL,
        GREATERTHEN,
        LESSTHEN,
        GREATERTHENOREQUAL,
        LESSTHENOREQUAL
    }

    private List<Object> conditionsValues = new ArrayList<>();

    private List<String> chains = new ArrayList<>();

    public DeleteQueryBuilder(String tableName) {
        this.tableName = tableName;
    }


    public DeleteQueryBuilder addEqualCondition(String condition, Object value) {
        //equalConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.EQUAL);
        conditionsValues.add(value);
        return this;
    }

    public DeleteQueryBuilder addLikeCondition(String condition, Object value) {
        //equalConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.LIKE);
        conditionsValues.add(value);
        return this;
    }


    public DeleteQueryBuilder addNotEqualCondition(String condition, Object value) {
        //notEqualConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.NOTEQUAL);
        conditionsValues.add(value);
        return this;
    }

    public DeleteQueryBuilder addGreaterThenCondition(String condition, Object value) {
        //greaterThenConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.GREATERTHEN);
        conditionsValues.add(value);

        return this;
    }

    public DeleteQueryBuilder addLessThenCondition(String condition, Object value) {
//        lessThenConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.LESSTHEN);
        conditionsValues.add(value);

        return this;
    }


    public Query build() {
        QueryBuilder queryBuilder = new QueryBuilder(conditions, conditionsType, conditionsValues,chains);
        String sql = queryBuilder.toSql();

        for (int i = 0; i < chains.size(); i++) {
            sql = sql.replaceFirst("\\$", chains.get(i));
        }

        Object[] values = queryBuilder.getValues();

        return new Query(sql, values);
    }


    public Object[] getValues() {
        return null;
    }


    public class QueryBuilder {

        private List<String> conditions;
        private List<ConditionType> conditionTypes;

        private List<Object> conditionsValues;
        private List<String> chains;

        private Integer limit;
        private String orderBy;
        private Boolean chainConditionsFLag = false;

        private QueryBuilder(List<String> conditions, List<ConditionType> conditionTypes , List<Object> conditionsValues , List<String> chains) {
            this.conditions = conditions;
            this.conditionsValues = conditionsValues;
            this.conditionTypes = conditionTypes;
            this.chains = chains;
        }

        public String toSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("DELETE FROM ")
                   .append(tableName);

            if (!conditions.isEmpty()) {
                builder.append(" WHERE ");

                for (int i = 0; i < conditions.size(); i++) {
                    chainQuery(builder);
                    String condition = conditions.get(i);
                    ConditionType conditionType = conditionTypes.get(i);

                    if (conditionType.equals(ConditionType.EQUAL)) {
                        builder.append(condition  + " = ? ");
                        chainConditionsFLag = true;
                    } else if (conditionType.equals(ConditionType.LIKE)) {
                        //TODO REFACTOR THE LIKE SYNTAX BASED ON DBMS
                        builder.append(condition  + " LIKE ? ");
                        chainConditionsFLag = true;

                    } else if (conditionType.equals(ConditionType.NOTEQUAL)) {
                        builder.append(condition  + " <> ? ");
                        chainConditionsFLag = true;
                    }
                    else if (conditionType.equals(ConditionType.GREATERTHEN)) {
                        builder.append(condition  + " > ? ");
                        chainConditionsFLag = true;
                    }
                    else if (conditionType.equals(ConditionType.LESSTHEN)) {
                        builder.append(condition  + " < ? ");
                        chainConditionsFLag = true;
                    }
                }
            }


            if (limit != null) {
                builder.append(" LIMIT ? ");
            }


            if (orderBy != null) {
                builder.append(" ORDER BY ");
                builder.append(orderBy);
            }

            return builder.toString();
        }

        private void chainQuery(StringBuilder stringBuilder) {
            if (chainConditionsFLag){
                stringBuilder.append(" $ ");
                chainConditionsFLag = false;
            }
        }

        public Object[] getValues() {
            if (this.limit != null)
                conditionsValues.add(limit);
            return conditionsValues.toArray();
        }

    }



}

package org.easyorm.datamapper.queryBuilders;

import java.util.*;

public class SelectQueryBuilder {
    private List<String> tables = new ArrayList<>();
    private List<String> columns = new ArrayList<>();


    private List<String> conditions = new ArrayList<>();
    private List<ConditionType> conditionsType = new ArrayList<>();

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

    private Integer limit;
    private String orderBy;

    public SelectQueryBuilder setTable(String table) {
        tables.add(table);
        return this;
    }

    public SelectQueryBuilder addColumn(String column) {
        columns.add(column);
        return this;
    }

    public SelectQueryBuilder addEqualCondition(String condition, Object value) {
        //equalConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.EQUAL);
        conditionsValues.add(value);
        return this;
    }

    public SelectQueryBuilder addLikeCondition(String condition, Object value) {
        //equalConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.LIKE);
        conditionsValues.add(value);
        return this;
    }


    public SelectQueryBuilder addNotEqualCondition(String condition, Object value) {
        //notEqualConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.NOTEQUAL);
        conditionsValues.add(value);
        return this;
    }
    public SelectQueryBuilder addGreaterThenCondition(String condition, Object value) {
        //greaterThenConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.GREATERTHEN);
        conditionsValues.add(value);

        return this;
    }
    public SelectQueryBuilder addLessThenCondition(String condition, Object value) {
//        lessThenConditions.add(condition);
        conditions.add(condition);
        conditionsType.add(ConditionType.LESSTHEN);
        conditionsValues.add(value);

        return this;
    }

    public SelectQueryBuilder addChainOperation(String chain) {
        chains.add(chain);

        return this;
    }

    public SelectQueryBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public SelectQueryBuilder setLimit(Integer limit) {
        this.limit = limit;

        return this;
    }


    public Query build() {
        QueryBuilder queryBuilder = new QueryBuilder(tables, columns, conditions, conditionsType, conditionsValues,limit,orderBy, chains);
        String sql = queryBuilder.toSql();

        for (int i = 0; i < chains.size(); i++) {
            sql = sql.replaceFirst("\\$", chains.get(i));
        }

        Object[] values = queryBuilder.getValues();

        return new Query(sql, values);
    }

    public class QueryBuilder {
        private List<String> tables;
        private List<String> columns;

        private List<String> conditions;
        private List<ConditionType> conditionTypes;

        private List<Object> conditionsValues;
        private List<String> chains;

        private Integer limit;
        private String orderBy;
        private Boolean chainConditionsFLag = false;

        public QueryBuilder(List<String> tables, List<String> columns, List<String> conditions, List<ConditionType> conditionTypes ,List<Object> conditionsValues ,Integer limit, String orderBy, List<String> chains) {
            this.tables = tables;
            this.columns = columns;
            this.conditions = conditions;
            this.conditionsValues = conditionsValues;
            this.conditionTypes = conditionTypes;
            this.chains = chains;
            this.limit = limit;
            this.orderBy = orderBy;
        }

        public String toSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT ");

            if (columns.isEmpty()) {
                //TODO
            } else {
                builder.append(String.join(",", columns));
            }

            builder.append(" FROM ");
            builder.append(String.join(",", tables));

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

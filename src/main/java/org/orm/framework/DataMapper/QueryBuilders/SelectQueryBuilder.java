package org.orm.framework.DataMapper.QueryBuilders;

import org.orm.framework.DataMapper1.methods.Query;

import java.util.*;
import java.util.stream.Collectors;

public class SelectQueryBuilder {
    private List<String> tables = new ArrayList<>();
    private List<String> columns = new ArrayList<>();

    private List<String> equalConditions = new ArrayList<>();
    private List<String> notEqualConditions = new ArrayList<>();
    private List<String> greaterThenConditions = new ArrayList<>();
    private List<String> lessThenConditions = new ArrayList<>();

    private List<Object> conditions = new ArrayList<>();

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
        equalConditions.add(condition);
        conditions.add(value);
        return this;
    }

    public SelectQueryBuilder addNotEqualCondition(String condition, Object value) {
        notEqualConditions.add(condition);
        conditions.add(value);
        return this;
    }
    public SelectQueryBuilder addGreaterThenCondition(String condition, Object value) {
        greaterThenConditions.add(condition);
        conditions.add(value);
        return this;
    }
    public SelectQueryBuilder addLessThenCondition(String condition, Object value) {
        lessThenConditions.add(condition);
        conditions.add(value);
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
        QueryBuilder queryBuilder = new QueryBuilder(tables, columns, equalConditions, notEqualConditions, greaterThenConditions, lessThenConditions, conditions ,limit,orderBy);
        String sql = queryBuilder.toSql();
        Object[] values = queryBuilder.getValues();

        return new Query(sql, values);
    }

    public class QueryBuilder {
        private List<String> tables;
        private List<String> columns;

        private List<String> equalConditions;
        private List<String> notEqualConditions;
        private List<String> greaterThenConditions ;
        private List<String> lessThenConditions;

        private List<Object> conditions;

        private Integer limit;
        private String orderBy;
        private Boolean flag = false;

        public QueryBuilder(List<String> tables, List<String> columns, List<String> equalConditions, List<String> notEqualConditions, List<String> greaterThenConditions, List<String> lessThenConditions,List<Object> conditions ,Integer limit, String orderBy) {
            this.tables = tables;
            this.columns = columns;
            this.equalConditions = equalConditions;
            this.notEqualConditions = notEqualConditions;
            this.greaterThenConditions = greaterThenConditions;
            this.lessThenConditions = lessThenConditions;
            this.conditions = conditions;
            this.limit = limit;
            this.orderBy = orderBy;
        }

        public String toSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT ");

            if (columns.isEmpty()) {

            } else {
                builder.append(String.join(",", columns));
            }

            builder.append(" FROM ");
            builder.append(String.join(",", tables));

            if (!equalConditions.isEmpty() || !notEqualConditions.isEmpty() || !greaterThenConditions.isEmpty() || !lessThenConditions.isEmpty())
                    builder.append(" WHERE ");


            if (!equalConditions.isEmpty()) {
                addAndToQuery(builder);
                builder.append(equalConditions.stream()
                        .map(condition -> condition  + " LIKE ?")
                        .collect(Collectors.joining(" AND ")));

                flag=true;
            }

            if (!notEqualConditions.isEmpty()) {
                addAndToQuery(builder);
                builder.append(notEqualConditions.stream()
                        .map(condition -> condition  + " <> ?")
                        .collect(Collectors.joining(" AND ")));
                flag=true;
            }

            if (!greaterThenConditions.isEmpty()) {
                addAndToQuery(builder);
                builder.append(greaterThenConditions.stream()
                        .map(condition -> condition  + " > ?")
                        .collect(Collectors.joining(" AND ")));
                flag=true;
            }

            if (!lessThenConditions.isEmpty()) {
                addAndToQuery(builder);
                builder.append(lessThenConditions.stream()
                        .map(condition -> condition  + " < ?")
                        .collect(Collectors.joining(" AND ")));
                flag=true;
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

        public Object[] getValues() {
            if (this.limit != null)
                conditions.add(limit);
            return conditions.toArray();
        }
        private void addAndToQuery(StringBuilder stringBuilder) {
            if (flag){
                stringBuilder.append(" AND ");
                flag = false;
            }
        }
    }
}

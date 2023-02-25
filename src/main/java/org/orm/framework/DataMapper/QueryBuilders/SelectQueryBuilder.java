package org.orm.framework.DataMapper.QueryBuilders;

import org.orm.framework.DataMapper.ObjectBuilders.Query;

import java.util.*;
import java.util.stream.Collectors;

public class SelectQueryBuilder {
    private  String table;
    private List<String> columns = new ArrayList<>();

    private Map<String, Object> conditions = new HashMap<>();

    private String orderBy;


    public SelectQueryBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public SelectQueryBuilder addColumn(String column) {
        columns.add(column);
        return this;
    }

    public SelectQueryBuilder addCondition(String condition, Object value) {
        conditions.put(condition, value);
        return this;
    }

    public SelectQueryBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public Query build() {
        QueryBuilder queryBuilder = new QueryBuilder(table, columns, conditions, orderBy);
        String sql = queryBuilder.toSql();
        Object[] values = queryBuilder.getValues();

        return new Query(sql, values);
    }


    public class QueryBuilder {
        private String table;
        private List<String> columns;
        private Map<String, Object> conditions;
        private String orderBy;

        public QueryBuilder(String table, List<String> columns, Map<String, Object> conditions, String orderBy) {
            this.table = table;
            this.columns = columns;
            this.conditions = conditions;
            this.orderBy = orderBy;
        }

        public String toSql() {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT ");

            if (columns.isEmpty()) {
                builder.append("*");
            } else {
                builder.append(String.join(",", columns));
            }

            builder.append(" FROM ");
            builder.append(table);

            if (!conditions.isEmpty()) {
                builder.append(" WHERE ");
                builder.append(conditions.entrySet().stream()
                        .map(entry -> entry.getKey() + " = ?")
                        .collect(Collectors.joining(" AND ")));
            }


            if (orderBy != null) {
                builder.append(" ORDER BY ");
                builder.append(orderBy);
            }

            return builder.toString();
        }

        public Object[] getValues() {
            List<Object> values = new ArrayList<>();
            conditions.forEach((cond, val) -> values.add(val));
            return values.toArray();
        }

    }
}

package org.orm.framework.DataMapper1.QueryBuilders;

import org.orm.framework.DataMapper.ObjectBuilders.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectQueryBuilder {
    private List<String> tables = new ArrayList<>();
    private List<String> columns = new ArrayList<>();
    private Map<String, Object> conditions = new HashMap<>();
    private String orderBy;

    public SelectQueryBuilder setTable(String table) {
        tables.add(table);
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
        QueryBuilder queryBuilder = new QueryBuilder(tables, columns, conditions, orderBy);
        String sql = queryBuilder.toSql();
        Object[] values = queryBuilder.getValues();

        return new Query(sql, values);
    }

    public class QueryBuilder {
        private List<String> tables;
        private List<String> columns;
        private Map<String, Object> conditions;
        private String orderBy;

        public QueryBuilder(List<String> tables, List<String> columns, Map<String, Object> conditions, String orderBy) {
            this.tables = tables;
            this.columns = columns;
            this.conditions = conditions;
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

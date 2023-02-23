package org.orm.framework.DataMapper.QueriyBuilders;

import java.util.ArrayList;
import java.util.List;

public class SelectQueryBuilder {
    private  String table;
    private List<String> columns = new ArrayList<>();
    private List<String> conditions = new ArrayList<>();
    private String orderBy;


    public SelectQueryBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public SelectQueryBuilder addColumn(String column) {
        columns.add(column);
        return this;
    }

    public SelectQueryBuilder addCondition(String condition) {
        conditions.add(condition);
        return this;
    }

    public SelectQueryBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public Query build() {
        return new Query(table, columns, conditions, orderBy);
    }


    public class Query {
        private String table;
        private List<String> columns;
        private List<String> conditions;
        private String orderBy;

        public Query(String table, List<String> columns, List<String> conditions, String orderBy) {
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
                builder.append(String.join(" AND ", conditions));
            }

            if (orderBy != null) {
                builder.append(" ORDER BY ");
                builder.append(orderBy);
            }

            return builder.toString();
        }
    }
}

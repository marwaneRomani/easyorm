package org.orm.framework.datamapper.QueryBuilders;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UpdateQueryBuilder {

    private String tableName;
    private Map<String, Object> columnValues;
    private Map<String, Object> whereConditions;

    public Map<String, Object> getColumnValues() {
        return columnValues;
    }

    public void setColumnValues(Map<String, Object> columnValues) {
        this.columnValues = columnValues;
    }

    public Map<String, Object> getWhereConditions() {
        return whereConditions;
    }

    public void setWhereConditions(LinkedHashMap<String, Object> whereConditions) {
        this.whereConditions = whereConditions;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public UpdateQueryBuilder(String tableName) {
        this.tableName = tableName;
        this.columnValues = new LinkedHashMap<>();
        this.whereConditions = new LinkedHashMap<>();
    }

    public UpdateQueryBuilder() {
        this.columnValues = new LinkedHashMap<>();
        this.whereConditions = new LinkedHashMap<>();
    }


    public UpdateQueryBuilder set(String columnName, Object value) {
        this.columnValues.put(columnName, value);
        return this;
    }

    public UpdateQueryBuilder where(String condition, Object value) {
        this.whereConditions.put(condition, value);
        return this;
    }

    public String build() {
        if (columnValues.isEmpty()) {
            throw new IllegalStateException("No columns to update specified.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(tableName).append(" SET ");
        sb.append(columnValues.entrySet().stream()
                .map(entry -> entry.getKey() + " = ?")
                .collect(Collectors.joining(", ")));

        if (!whereConditions.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(whereConditions.entrySet().stream()
                    .map(entry -> entry.getKey() + " = ?")
                    .collect(Collectors.joining(" AND ")));
        }

        return sb.toString();
    }

    public Object[] getValues() {
        List<Object> values = new ArrayList<>();

        columnValues.forEach((column, value) -> values.add(value));

        whereConditions.forEach((cond, val) -> values.add(val));

        return values.toArray();
    }
}

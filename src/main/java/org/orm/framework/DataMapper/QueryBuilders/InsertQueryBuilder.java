package org.orm.framework.DataMapper.QueryBuilders;
import java.util.ArrayList;
import java.util.List;

public class InsertQueryBuilder {
    private String tableName;
    private List<String> columns = new ArrayList<>();
    private List<Object> values = new ArrayList<>();

    public InsertQueryBuilder(String tableName) {
        this.tableName = tableName;
    }

    public InsertQueryBuilder column(String columnName) {
        columns.add(columnName);
        return this;
    }

    public InsertQueryBuilder value(Object value) {
        values.add(value);
        return this;
    }

    public String build() {
        if (columns.isEmpty()) {
            throw new IllegalArgumentException("At least one column must be specified.");
        }

        if (columns.size() != values.size()) {
            throw new IllegalArgumentException("The number of values must match the number of columns.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(String.join(", ", columns))
                .append(") VALUES (");

        for (int i = 0; i < values.size(); i++) {
            sb.append("?");
            if (i < values.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    public Object[] getValues() {
        return values.toArray();
    }
}

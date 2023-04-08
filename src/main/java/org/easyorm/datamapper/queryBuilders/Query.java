package org.easyorm.datamapper.queryBuilders;

public class Query {
    private String query;
    private Object[] values;

    public Query(String query, Object[] values) {
        this.query = query;
        this.values = values;
    }

    public String getQuery() {
        return query;
    }
    public Object[] getValues() { return values; }

}

package org.easyorm.databasebuilder.builders;

import org.easyorm.databasebuilder.dialects.Dialect;

import java.sql.Connection;

public interface StategyBuilder {
    void setConnection(Connection connection);
    void setDialect(Dialect dialect);
    void build();
}

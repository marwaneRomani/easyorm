package org.orm.framework.databasebuilder.builders;

import org.orm.framework.databasebuilder.dialects.Dialect;

import java.sql.Connection;

public interface StategyBuilder {
    void setConnection(Connection connection);
    void setDialect(Dialect dialect);
    void build();
}

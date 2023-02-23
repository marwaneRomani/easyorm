package org.orm.framework.DatabaseBuilder.Builders;

import org.orm.framework.DatabaseBuilder.Dialects.Dialect;

import java.sql.Connection;

public interface StategyBuilder {
    void setConnection(Connection connection);
    void setDialect(Dialect dialect);
    void build();
}

package org.orm.framework.DatabaseBuilder.Builders;

import org.orm.framework.DatabaseBuilder.Dialects.Dialect;

import java.sql.Connection;

public class CreateDropBuilder implements StategyBuilder {
    private Connection connection;
    private Dialect dialect;

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void build() {

    }
}

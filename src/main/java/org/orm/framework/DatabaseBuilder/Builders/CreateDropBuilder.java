package org.orm.framework.DatabaseBuilder.Builders;

import org.orm.framework.DatabaseBuilder.Dialects.Dialect;
import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.orm.framework.TransactionsManager.Transaction.wrapInTransaction;

public class CreateDropBuilder extends CreateBuilder  implements StategyBuilder {
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
        drop();
        super.build();
    }

    private void drop() {
        List<String> tables = new ArrayList<>();
        EntitiesDataSource.getModelsSchemas()
                          .values()
                          .forEach(entity -> {
                              tables.add(entity.getName());
                          });
        dialect.getDropTablesSyntax((String[]) tables.toArray());
    }
}

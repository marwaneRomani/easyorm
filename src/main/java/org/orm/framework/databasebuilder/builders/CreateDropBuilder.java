package org.orm.framework.databasebuilder.builders;

import org.orm.framework.databasebuilder.dialects.Dialect;
import org.orm.framework.entitiesdtasource.EntitiesDataSource;
import org.orm.framework.transactionsmanager.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
        super.setConnection(connection);
        super.setDialect(dialect);
        Transaction.wrapInTransaction(connection,  drop(), super.createTables(),super.createRelations());
    }

    private Consumer<Connection> drop() {
        return connection -> {
            List<String> tables = new ArrayList<>();
            EntitiesDataSource.getModelsSchemas()
                    .values()
                    .forEach(entity -> {
                        tables.add(entity.getName());
                    });
            String dropTablesSyntax = dialect.getDropTablesSyntax(tables);

            try {
                Statement statement = connection.createStatement();
                statement.execute(dropTablesSyntax);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }
}

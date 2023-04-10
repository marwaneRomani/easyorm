package org.easyorm.databasebuilder.builders;

import org.easyorm.applicationstate.ApplicationState;
import org.easyorm.databasebuilder.dialects.Dialect;
import org.easyorm.entitiesdtasource.EntitiesDataSource;
import org.easyorm.transactionsmanager.Transaction;

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

            String[] words = ApplicationState.getState().getUrl().split("/");
            String dbName = words[words.length - 1];

            String dropDatabaseSyntax  = "DROP DATABASE `" + dbName  + "`" ;//TODO Need to be refactored;
            String createDatabaseSyntax  = "CREATE SCHEMA `" + dbName  + "`" ;//TODO Need to be refactored;
            String useDatabase = "USE " + dbName + ";";

            try {
                Statement statement = connection.createStatement();
                statement.execute(dropDatabaseSyntax);
                statement.execute(createDatabaseSyntax);
                statement.executeUpdate(useDatabase);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }
}

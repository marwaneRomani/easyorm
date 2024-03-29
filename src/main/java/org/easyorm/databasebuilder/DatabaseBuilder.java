package org.easyorm.databasebuilder;

import org.easyorm.databasebuilder.builders.CreateBuilder;
import org.easyorm.databasebuilder.builders.CreateDropBuilder;
import org.easyorm.databasebuilder.builders.StategyBuilder;
import org.easyorm.databasebuilder.dialects.Dialect;
import org.easyorm.databasebuilder.dialects.H2Dialect;
import org.easyorm.databasebuilder.dialects.MySQLDialect;
import org.easyorm.databasebuilder.dialects.PostgreSqlDialect;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class DatabaseBuilder {

    private final Map<String , Class<? extends StategyBuilder> > strategies = new HashMap<>(){{
        put("create", CreateBuilder.class);
        put("create-drop", CreateDropBuilder.class);
    }};

    private final Map<String, Class<? extends Dialect>> dialects = new HashMap<>() {{
        put("mysql", MySQLDialect.class);
        put("h2", H2Dialect.class);
        put("postgresql", PostgreSqlDialect.class);
    }};

    public void build(String strategy, String dialect, Connection connection) throws Exception {
        Dialect dbDialect = dialects.get(dialect).newInstance();
        StategyBuilder dbStrategy = strategies.get(strategy.toLowerCase()).newInstance();


        dbStrategy.setDialect(dbDialect);
        dbStrategy.setConnection(connection);

        dbStrategy.build();
    }
}

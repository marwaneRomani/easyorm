package org.orm.framework.DatabaseBuilder.Builders;

import org.orm.framework.DatabaseBuilder.Dialects.Dialect;
import org.orm.framework.EntitiesDataSource.EntitiesDataSource;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.AttributeList;
import org.orm.framework.ModelsMapper.FieldsMapper.PrimaryKey.PrimaryKey;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.ManyToMany;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.OneToMany;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.OneToOne;
import org.orm.framework.ModelsMapper.FieldsMapper.Relation.Relation;
import org.orm.framework.TransactionsManager.Transaction;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.orm.framework.TransactionsManager.Transaction.wrapInTransaction;

public class CreateBuilder implements StategyBuilder {
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

    private Consumer<Connection> createTables() {
        return connection -> {
            for (Entity entity : EntitiesDataSource.getModelsSchemas().values()) {
                String createTableSyntax =
                        dialect.getCreateTableSyntax(entity.getName(),
                                entity.getNormalAttributes(),
                                Collections.singletonList(entity.getPrimaryKey()));

                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(createTableSyntax);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        };
    }

    private Consumer<Connection> createRelations() {
        return connection -> {
            EntitiesDataSource
                    .getModelsSchemas()
                    .values()
                    .forEach(entity -> {
                        for (Relation relation : entity.getRelations()) {
                            try {
                                String[] sql = addConstraint(relation);
                                for (int i = 0; i < sql.length; i++) {
                                    Statement statement = connection.createStatement();
                                    statement.executeUpdate(sql[i]);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
        };
    }



    protected String[] addConstraint(Relation relation) throws Exception {
        switch (relation.getClass().getSimpleName()) {
            case "OneToOne"  : return evaluateOneToOneRelation(relation);

            case "OneToMany"   : return evaluateOneToManyRelation(relation);

            case "ManyToMany" : return evaluateManyToManyRelation(relation);

            default: return null;
        }
    }


    private String[] evaluateOneToOneRelation(Relation relation) throws Exception {
        System.out.println(relation.toString());


        Method getFirst = OneToOne.class.getMethod("getFirst");
        Method getLast = OneToOne.class.getMethod("getSecond");

        Attribute first = (Attribute) getFirst.invoke(relation);
        Attribute last = (Attribute) getLast.invoke(relation);

        String firstTable = getEntityName(first) + "";

        PrimaryKey foreignKey = EntitiesDataSource
                .getModelsSchemas()
                .get(last.getClazz().getSimpleName())
                .getPrimaryKey();


        Attribute newColumn = new Attribute(first);

        newColumn.setClazz(first.getClazz());
        newColumn.setName(first.getName());
        newColumn.setOriginalName(first.getOriginalName());
        newColumn.setInherited(first.isInherited());
        newColumn.setNullable(false);
        newColumn.setUnique(true);
        newColumn.setAutoIncrement(false);

        newColumn.setType(foreignKey.getType());
        newColumn.setLength(foreignKey.getLength());
        newColumn.setDbType(foreignKey.getDbType());

        String addColumnSyntax = dialect.getAddColumnSyntax(firstTable, newColumn);

        String addForeignKeySyntax = dialect.getAddForeignKeySyntax(firstTable, newColumn.getName(), List.of(newColumn.getName()), last.getClazz().getSimpleName().toLowerCase(), List.of(foreignKey.getName()));

        String[] statements = { addColumnSyntax, addForeignKeySyntax };

        return statements;

    }

    private String[] evaluateOneToManyRelation(Relation relation) throws Exception {
        System.out.println(relation.toString());
        Method getSmall = OneToMany.class.getMethod("getSmall");
        Method getBig = OneToMany.class.getMethod("getBig");

        Attribute smallAttr = (Attribute) getSmall.invoke(relation);
        Attribute bigAttr = (Attribute) getBig.invoke(relation);

        String smallTableName = getEntityName(smallAttr);

        String bigEntityName = getEntityName(smallAttr);

        String bigEntityIdName = EntitiesDataSource
                .getModelsSchemas()
                .get(smallAttr.getType())
                .getPrimaryKey()
                .getName();

        PrimaryKey foreignKey = EntitiesDataSource
                .getModelsSchemas()
                .get(smallAttr.getType())
                .getPrimaryKey();

        Attribute newColumn = new Attribute(smallAttr);

        newColumn.setClazz(smallAttr.getClazz());
        newColumn.setName(smallAttr.getName());
        newColumn.setOriginalName(smallAttr.getOriginalName());
        newColumn.setInherited(smallAttr.isInherited());
        newColumn.setNullable(false);
        newColumn.setUnique(true);
        newColumn.setAutoIncrement(false);

        newColumn.setType(foreignKey.getType());
        newColumn.setLength(foreignKey.getLength());
        newColumn.setDbType(foreignKey.getDbType());

        String addColumnSyntax = dialect.getAddColumnSyntax(smallTableName, newColumn);

        String addForeignKeySyntax = dialect.getAddForeignKeySyntax(smallTableName, newColumn.getName(), List.of(newColumn.getName()), bigAttr.getClazz().getSimpleName().toLowerCase(), List.of(foreignKey.getName()));

        String[] statements = { addColumnSyntax, addForeignKeySyntax };

        return statements;
    }

    private String[] evaluateManyToManyRelation(Relation relation) throws Exception {
        System.out.println(relation);

        Method getFirst = ManyToMany.class.getMethod("getFirst");
        Method getLast = ManyToMany.class.getMethod("getLast");

        AttributeList first = (AttributeList) getFirst.invoke(relation);
        AttributeList last = (AttributeList) getLast.invoke(relation);

        // We create a new Entity named Between

        String firstTable = getEntityName(first);
        String lastTableName = getEntityName(last);

        PrimaryKey primaryKeyOfFirstEntity = EntitiesDataSource
                .getModelsSchemas()
                .get(first.getGenericType())
                .getPrimaryKey();
        PrimaryKey primaryKeyOfLastEntity = EntitiesDataSource
                .getModelsSchemas()
                .get(last.getGenericType())
                .getPrimaryKey();

        StringBuilder script = new StringBuilder();

        String createTableSyntax = dialect.getCreateTableSyntax(firstTable + "_" + lastTableName, List.of(), List.of(primaryKeyOfFirstEntity, primaryKeyOfLastEntity));

        System.out.println(createTableSyntax);

        return new String[] { script.toString() };
    }



    private String getEntityName(Attribute attribute) {
        return EntitiesDataSource
                .getModelsSchemas()
                .get(attribute.getClazz().getSimpleName())
                .getName()
                .toLowerCase();
    }


    @Override
    public void build() {

        wrapInTransaction(connection, createTables() ,createRelations());
    }
}

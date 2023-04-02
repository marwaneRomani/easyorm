package org.orm.framework.databasebuilder.builders;

import org.orm.framework.databasebuilder.dialects.Dialect;
import org.orm.framework.entitiesdtasource.EntitiesDataSource;
import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.orm.framework.modelsmapper.fieldsmapper.primarykey.PrimaryKey;
import org.orm.framework.modelsmapper.fieldsmapper.relation.ManyToMany;
import org.orm.framework.modelsmapper.fieldsmapper.relation.OneToMany;
import org.orm.framework.modelsmapper.fieldsmapper.relation.OneToOne;
import org.orm.framework.modelsmapper.fieldsmapper.relation.Relation;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.orm.framework.transactionsmanager.Transaction.wrapInTransaction;

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

    protected Consumer<Connection> createTables() {
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

    protected Consumer<Connection> createRelations() {
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


    protected String[] evaluateOneToOneRelation(Relation relation) throws Exception {
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

    protected String[] evaluateOneToManyRelation(Relation relation) throws Exception {
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

    protected String[] evaluateManyToManyRelation(Relation relation) throws Exception {

        Method getFirst = ManyToMany.class.getMethod("getFirst");
        Method getLast = ManyToMany.class.getMethod("getLast");

        AttributeList first = (AttributeList) getFirst.invoke(relation);
        AttributeList last = (AttributeList) getLast.invoke(relation);

        // We create a new Entity named first_last

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

        PrimaryKey firstPrimaryKey = new PrimaryKey();
        firstPrimaryKey.setName(lastTableName + '_' + primaryKeyOfFirstEntity.getName());
        firstPrimaryKey.setType(primaryKeyOfFirstEntity.getType());
        firstPrimaryKey.setOriginalName(primaryKeyOfFirstEntity.getOriginalName());
        firstPrimaryKey.setAutoIncrement(primaryKeyOfFirstEntity.isAutoIncrement());
        firstPrimaryKey.setLength(primaryKeyOfFirstEntity.getLength());
        firstPrimaryKey.setDbType(primaryKeyOfFirstEntity.getDbType());

        PrimaryKey lastPrimaryKey = new PrimaryKey();
        lastPrimaryKey.setName(firstTable + '_' + primaryKeyOfLastEntity.getName());
        lastPrimaryKey.setType(primaryKeyOfLastEntity.getType());
        lastPrimaryKey.setOriginalName(primaryKeyOfLastEntity.getOriginalName());
        lastPrimaryKey.setAutoIncrement(primaryKeyOfLastEntity.isAutoIncrement());
        lastPrimaryKey.setLength(primaryKeyOfLastEntity.getLength());
        lastPrimaryKey.setDbType(primaryKeyOfLastEntity.getDbType());

        System.out.println(firstPrimaryKey.getName());
        System.out.println(lastPrimaryKey.getName());

        String createTableSyntax = dialect.getCreateTableSyntax(((ManyToMany)relation).getTableName(), List.of(), List.of(firstPrimaryKey, lastPrimaryKey));


        return new String[] { createTableSyntax };
    }



    protected String getEntityName(Attribute attribute) {
        return EntitiesDataSource
                .getModelsSchemas()
                .get(attribute.getClazz().getSimpleName())
                .getName()
                .toLowerCase();
    }


    @Override
    public void build() {
        wrapInTransaction(connection, createTables(), createRelations());
    }
}

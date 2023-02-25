package org.orm.framework.DatabaseBuilder.Dialects;

import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.PrimaryKey.PrimaryKey;

import java.util.List;
import java.util.UUID;

public class MySQLDialect implements Dialect {
    @Override
    public String getCreateTableSyntax(
                                       String tableName,
                                       List<Attribute> columns,
                                       List<PrimaryKey> primaryKeys)
    {
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE `").append(tableName).append("` (\n");

        for (PrimaryKey primaryKey : primaryKeys) {
            builder.append(primaryKey.getName()).append(" ").append(primaryKey.getDbType());
            if (primaryKey.isAutoIncrement()) {
                builder.append(" ").append(getAutoIncrementKeyword());
            }
            builder.append(",\n");
        }

        for (Attribute column : columns) {
            builder.append(column.getName()).append(" ").append(column.getDbType());
            if (column.isAutoIncrement()) {
                builder.append(" ").append(getAutoIncrementKeyword());
            }
            builder.append(",\n");
        }
        builder.append("PRIMARY KEY (");
        for (PrimaryKey primaryKey : primaryKeys) {
            builder.append(primaryKey.getName()).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length()); // remove last comma and space
        builder.append(")\n)");
        return builder.toString();
    }

    @Override
    public String getAddColumnSyntax(String tableName, Attribute column) {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `").append(tableName).append("` ADD COLUMN ")
                .append(column.getName()).append(" ").append(column.getDbType());
        if (column.isAutoIncrement()) {
            builder.append(" ").append(getAutoIncrementKeyword());
        }
        
        return builder.toString();
    }

    @Override
    public String getDropTableSyntax(String tableName) {
        return "DROP TABLE `".concat(tableName).concat("`");
    }

    @Override
    public String getDropTablesSyntax(List<String> tablesName) {
        StringBuilder sb = new StringBuilder("DROP TABLE ");
        for (int i = 0; i < tablesName.size(); i++) {
            sb.append("`" + tablesName.get(i)).append("`");
            if (i < tablesName.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public String getAddPrimaryKeySyntax(String tableName, List<String> primaryKeys) {
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `").append(tableName).append("` ADD PRIMARY KEY (");
        for (String primaryKey : primaryKeys) {
            builder.append(primaryKey).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String getAddForeignKeySyntax(String tableName, String foreignKeyName, List<String> localColumns, String referencedTable, List<String> referencedColumns) {
        System.out.println(UUID.randomUUID() + foreignKeyName );
        StringBuilder builder = new StringBuilder();
        builder.append("ALTER TABLE `").append(tableName).append("` ADD CONSTRAINT `")
                .append(UUID.randomUUID() + foreignKeyName ).append("` FOREIGN KEY (");
        for (String localColumn : localColumns) {
            builder.append(localColumn).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length()); // remove last comma and space
        builder.append(") REFERENCES `").append(referencedTable).append("` (");
        for (String referencedColumn : referencedColumns) {
            builder.append(referencedColumn).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length()); // remove last comma and space
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String getAutoIncrementKeyword() {
        return "AUTO_INCREMENT";
    }
}

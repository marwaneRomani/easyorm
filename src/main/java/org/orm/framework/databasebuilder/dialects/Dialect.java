package org.orm.framework.databasebuilder.dialects;

import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;
import org.orm.framework.modelsmapper.fieldsmapper.primarykey.PrimaryKey;

import java.util.List;

public interface Dialect {

    String getCreateTableSyntax(String tableName, List<Attribute> columns, List<PrimaryKey> primaryKeys);
    String getAddColumnSyntax(String tableName,  Attribute column);
    String getDropTableSyntax(String tableName);
    String getDropTablesSyntax(List<String> tablesName);
    String getAddPrimaryKeySyntax(String tableName, List<String> primaryKeys);
    String getAddForeignKeySyntax(String tableName, String foreignKeyName, List<String> localColumns, String referencedTable, List<String> referencedColumns);
    String getAutoIncrementKeyword();

}

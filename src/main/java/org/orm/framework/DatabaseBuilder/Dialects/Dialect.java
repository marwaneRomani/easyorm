package org.orm.framework.DatabaseBuilder.Dialects;

import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.PrimaryKey.PrimaryKey;

import java.util.List;

public interface Dialect {

    String getCreateTableSyntax(String tableName, List<Attribute> columns, List<PrimaryKey> primaryKeys);
    String getAddColumnSyntax(String tableName,  Attribute column);
    String getDropTableSyntax(String tableName);
    String getDropTablesSyntax(String... tablesName);

    String getAddPrimaryKeySyntax(String tableName, List<String> primaryKeys);
    String getAddForeignKeySyntax(String tableName, String foreignKeyName, List<String> localColumns, String referencedTable, List<String> referencedColumns);
    String getAutoIncrementKeyword();

}

package org.orm.framework.datamapper1.jdbctemplate;


import org.orm.framework.datamapper1.utils.SettersInvoke;
import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;
import org.orm.framework.modelsmapper.fieldsmapper.primarykey.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RowMapper {
    public static Object mapRows(ResultSet resultSet, Entity entity) throws Exception {
        Object object = Class.forName(entity.getModel().getName()).newInstance();

        // Set the primary key
        try {
            SettersInvoke.setPrimaryKey(entity.getPrimaryKey(),
                    object,
                    (MapResultSetValue.getResultSetValue(entity.getPrimaryKey(), resultSet))
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        entity.getNormalAttributes()
              .stream()
              .forEach(attribute -> {
                  attribute.getType();
                  try {
                      SettersInvoke.setAttribute(attribute,
                              object,
                              (MapResultSetValue.getResultSetValue(attribute, resultSet))
                      );
                  } catch (SQLException e) {
                      throw new RuntimeException(e);
                  }
          });

        return entity.getModel().cast(object);
    }

    public static void mapRows(ResultSet resultSet, Entity entity, Object object, List<Object> autoIncrementFields, Map<Object, Object> autoIcrementsWithValue) throws Exception {

        int rsCounter = 0;
        while (resultSet.next()) {
            Object field = autoIncrementFields.get(rsCounter);
            Object value = null;

            if (entity.getPrimaryKey().isAutoIncrement() && rsCounter == 0)
                value = MapResultSetValue.getResultSetValue(entity.getPrimaryKey(),resultSet);
            else
                value = MapResultSetValue.getResultSetValue((Attribute) field,resultSet);

            autoIcrementsWithValue.put(field, value);
            rsCounter++;
        }


        if (entity.getPrimaryKey().isAutoIncrement())
            SettersInvoke.setPrimaryKey(
                        entity.getPrimaryKey(),
                        object,
                        autoIcrementsWithValue.get(entity.getPrimaryKey()
                    )
            );

        autoIncrementFields.forEach(attribute -> {
            if (!(attribute instanceof PrimaryKey))
                SettersInvoke.setAttribute((Attribute) attribute,object, autoIcrementsWithValue.get(attribute));
        });
    }

}

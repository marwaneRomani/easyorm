package org.orm.framework.DataMapper.JdbcTemplate;


import lombok.SneakyThrows;
import org.orm.framework.DataMapper1.Utils.SettersInvoke;
import org.orm.framework.EntitiesDataSource.Entity;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.PrimaryKey.PrimaryKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowMapper {
    public static Object mapRows(ResultSet resultSet, Entity entity) throws Exception {
        Object object = Class.forName(entity.getModel().getName()).newInstance();
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

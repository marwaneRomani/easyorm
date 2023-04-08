package org.easyorm.datamapper.jdbctemplate;

import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.primarykey.PrimaryKey;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapResultSetValue {
    public static Object getResultSetValue(Attribute attribute, ResultSet resultSet) throws SQLException {
        switch(attribute.getType()){
            case "Boolean" :
                return resultSet.getBoolean(attribute.getName());

            case "Byte" :
                return resultSet.getByte(attribute.getName());

            case "Short" :
                return resultSet.getShort(attribute.getName());

            case "Integer":
                return resultSet.getInt(attribute.getName());

            case "Long" :
                return resultSet.getLong(attribute.getName());

            case "Float" :
                return resultSet.getFloat(attribute.getName());

            case "Double" :
                return resultSet.getDouble(attribute.getName());

            case "String":
                return resultSet.getString(attribute.getName());

            case "Date" :
                return resultSet.getDate(attribute.getName());

            default:
                return null;
        }
    }

    public static Object getResultSetValue(PrimaryKey primaryKey, ResultSet resultSet) throws SQLException {
        switch(primaryKey.getType()){
            case "Boolean" :
                return resultSet.getBoolean(primaryKey.getOriginalName());

            case "Byte" :
                return resultSet.getByte(primaryKey.getOriginalName());

            case "Short" :
                return resultSet.getShort(primaryKey.getOriginalName());

            case "Integer":
                return resultSet.getInt(primaryKey.getName());

            case "Long" :
                return resultSet.getLong(primaryKey.getOriginalName());

            case "Float" :
                return resultSet.getFloat(primaryKey.getOriginalName());

            case "Double" :
                return resultSet.getDouble(primaryKey.getOriginalName());

            case "String":
                return resultSet.getString(primaryKey.getOriginalName());

            case "Date" :
                return resultSet.getDate(primaryKey.getOriginalName());

            default:
                return null;
        }
    }

    public static Object getResultSetValue(PrimaryKey primaryKey, ResultSet resultSet, int rowCount) throws SQLException {
        switch(primaryKey.getType()){
            case "Boolean" :
                return resultSet.getBoolean(rowCount);

            case "Byte" :
                return resultSet.getByte(rowCount);

            case "Short" :
                return resultSet.getShort(rowCount);

            case "Integer":
                return resultSet.getInt(rowCount);

            case "Long" :
                return resultSet.getLong(rowCount);

            case "Float" :
                return resultSet.getFloat(rowCount);

            case "Double" :
                return resultSet.getDouble(rowCount);

            case "String":
                return resultSet.getString(rowCount);

            case "Date" :
                return resultSet.getDate(rowCount);

            default:
                return null;
        }
    }

}

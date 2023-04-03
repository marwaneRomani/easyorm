package org.orm.framework.datamapper1.jdbctemplate;


import org.orm.framework.datamapper1.utils.SettersInvoke;
import org.orm.framework.entitiesdtasource.Entity;

import java.sql.*;
import java.util.*;

public class JdbcTemplateImpl implements JdbcTemplate {

    private Connection connection;

    public JdbcTemplateImpl(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public Object queryForObject(String query, Entity entity) {

        //execute query
        ResultSet resultSet = this.query(query, null);

        //map to target model
        Object result = this.mapResultToObject(resultSet, entity);

        return result;
    }

    @Override
    public Object queryForObject(String query, Entity entity, Object[] params) {

        //execute query
        ResultSet resultSet = this.query(query, params);

        //map to target model
        Object result = this.mapResultToObject(resultSet, entity);

        return result;
    }

    @Override
    public List<Object> queryForList(String query, Entity entity) throws Exception {
        List<Object> objects = new ArrayList<>();

        ResultSet resultSet = this.query(query, null);

        while (resultSet.next()) {
            objects.add(RowMapper.mapRows(resultSet, entity));
        }

        return objects;
    }

    @Override
    public List<Object> queryForList(String query, Entity entity,Object[] params ) throws Exception {
        List<Object> objects = new ArrayList<>();

        ResultSet resultSet = this.query(query, params);

        while (resultSet.next()) {
            objects.add(RowMapper.mapRows(resultSet, entity));
        }

        return objects;
    }


    private ResultSet query(String query, Object[] params) {
        try {
            if (params == null) {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                return resultSet;
            }
            else {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                fillPreparedStatement(ps,params);

                ResultSet resultSet = ps.executeQuery();


                return  resultSet;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nonQuery(String query, Object[] params, Entity entity, Object objectToPersist) {
        try {
            PreparedStatement ps = null;

            if (entity.getPrimaryKey().isAutoIncrement())
                ps = connection.prepareStatement(query , Statement.RETURN_GENERATED_KEYS);
            else
                ps = connection.prepareStatement(query);

            if (params != null)
                fillPreparedStatement(ps,params);

            int result = ps.executeUpdate();

            if (entity.getPrimaryKey().isAutoIncrement()) {
                if (result > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if(rs.next()) {
                        Object autoIncrementValue = MapResultSetValue.getResultSetValue(entity.getPrimaryKey(),rs, 1);
                        SettersInvoke.setPrimaryKey(entity.getPrimaryKey(), objectToPersist, autoIncrementValue);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int nonQuery(String query, Object[] params) {
        try {
            PreparedStatement ps = connection.prepareStatement(query);

            if (params != null)
                fillPreparedStatement(ps,params);

            int result = ps.executeUpdate();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object mapResultToObject(ResultSet resultSet, Entity entity) {
        try {
            Object object = null;
            while (resultSet.next())
                object = RowMapper.mapRows(resultSet, entity);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void fillPreparedStatement(PreparedStatement ps, Object[] arguments) throws SQLException {
        for (int i = 0; i < arguments.length; i++) {

            switch(arguments[i].getClass().getSimpleName()) {
                case "Boolean" :
                    ps.setBoolean(
                            i + 1,Boolean.parseBoolean(arguments[i].toString())
                    );
                    break;

                case "Byte" :
                    ps.setByte(
                            i + 1, Byte.parseByte(arguments[i].toString())
                    );
                    break;

                case "Short" :
                    ps.setShort(
                            i + 1, Short.parseShort(arguments[i].toString())
                    );
                    break;

                case "Integer":
                    ps.setInt(
                            i + 1, Integer.parseInt(arguments[i].toString())
                    );
                    break;

                case "Long" :
                    ps.setLong(
                            i + 1, Long.parseLong(arguments[i].toString())
                    );
                    break;

                case "Float" :
                    ps.setFloat(
                            i + 1, Float.parseFloat(arguments[i].toString())
                    );
                    break;

                case "Double" :
                    ps.setDouble(
                            i + 1, Double.parseDouble(arguments[i].toString())
                    );
                    break;

                case "String":
                    ps.setString(
                            i + 1, arguments[i].toString()
                    );
                    break;

                case "Date" :
                    ps.setDate(
                            i + 1, new java.sql.Date(((java.util.Date)arguments[i]).getTime())
                    );
                    break;
            }
        };
    }
}

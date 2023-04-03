package org.orm.framework.DataMapper1.JdbcTemplate;



import org.orm.framework.EntitiesDataSource.Entity;

import java.sql.SQLException;
import java.util.List;

public interface JdbcTemplate {
    Object queryForObject(String sql, Entity entity);
    Object queryForObject(String sql, Entity entity, Object[] params);

    List<Object> queryForList(String sql, Entity entity) throws Exception;
    List<Object> queryForList(String sql, Entity entity, Object[] params) throws SQLException, Exception;

    void nonQuery(String query, Object[] params, Entity entity,Object objectToPersiste);
    int nonQuery(String query, Object[] params);
}

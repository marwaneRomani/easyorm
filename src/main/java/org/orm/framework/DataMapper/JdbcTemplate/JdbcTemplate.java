package org.orm.framework.DataMapper.JdbcTemplate;



import org.orm.framework.EntitiesDataSource.Entity;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface JdbcTemplate {
    Object queryForObject(String sql, Entity entity);
    Object queryForObject(String sql, Entity entity, Object[] params);

    List<Object> queryForList(String sql, Entity entity) throws Exception;
    List<Object> queryForList(String sql, Object[] params, Entity entity) throws SQLException;

    void nonQuery(String query, Object[] params, Entity entity,Object objectToPersiste);
}

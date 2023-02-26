package org.orm.framework.DataMapper;

import org.orm.framework.ConnectionsPool.ConnectionPool;
import org.orm.framework.DataMapper.ObjectBuilders.ObjectBuilder;

import java.sql.Connection;

public class DataMapper {
    public static <T> ObjectBuilder<T> buildObject(Class<T> model) {
        return new ObjectBuilder<>(model);
    }
}

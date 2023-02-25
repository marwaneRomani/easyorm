package org.orm.framework.DataMapper;

import org.orm.framework.DataMapper.ObjectBuilders.ObjectBuilder;

public class DataMapper {
    public static <T> ObjectBuilder<T> buildObject(Class<T> model) {
        return new ObjectBuilder<>(model);
    }
}

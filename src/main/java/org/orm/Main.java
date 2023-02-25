package org.orm;

import org.orm.framework.DataMapper.DataMapper;
import org.orm.framework.OrmApplication;
import org.orm.models.B;

public class Main {
    public static void main(String[] args) {
        OrmApplication.run();

        DataMapper
            .buildObject(B.class)
            .findById(2)
            .get("as");
    }
}
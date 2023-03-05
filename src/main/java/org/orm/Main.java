package org.orm;

import org.orm.framework.DataMapper1.ObjectBuilder;
import org.orm.framework.OrmApplication;
import org.orm.models.A;
import org.orm.models.B;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        OrmApplication
                .run();

        B b = new B();
        b.setId(1);
        b.setNameB("first B");

        A a = new A();
        a.setNameA("first A");



        b.setAs(List.of(a));

        OrmApplication
                .buildObject(A.class)
                .save(a);
        System.out.println(a.getIdLong());
    }
}

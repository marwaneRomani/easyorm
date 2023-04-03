package org.orm.framework.transactionsmanager;

import org.orm.framework.datamapper1.methods.find.Find;
import org.orm.framework.datamapper1.methods.save.Save;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Consumer;

public class Transaction {
    public static void wrapInTransaction(Connection conn, Consumer<Connection>... codes) {
        try {
            conn.setAutoCommit(false);

            Arrays.stream(codes)
                    .forEach(code -> {
                        code.accept(conn); // execute the original method code
                    });

            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static void wrapMethodInTransaction(Connection conn, Save saveObject, Method method, Object ...args) {
        try {
            conn.setAutoCommit(false);

            method.invoke(saveObject, args);

            conn.commit();
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static void wrapMethodInTransaction(Connection conn, Find findObject, Method method, Object ...args) {
        try {
            conn.setAutoCommit(false);

            method.invoke(findObject, args);

            conn.commit();
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }


}

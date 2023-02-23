package org.orm.framework.TransactionsManager;

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
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}

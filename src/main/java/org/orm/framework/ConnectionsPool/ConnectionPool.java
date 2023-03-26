package org.orm.framework.ConnectionsPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
        private static ConnectionPool pool = null;

        private String url;
        private String username;
        private String password;
        private int maxPoolSize;
        private int currentPoolSize;
        private List<Connection> availableConnections;
        private List<Connection> usedConnections;

        private ConnectionPool(String url, String username, String password, int maxPoolSize) {
            this.url = url;
            this.username = username;
            this.password = password;
            this.maxPoolSize = maxPoolSize;
            this.currentPoolSize = 0;
            this.availableConnections = new ArrayList<>();
            this.usedConnections = new ArrayList<>();
        }

        public static ConnectionPool getInstance(String url, String username, String password, int maxPoolSize) {
            if (pool == null)
                pool = new ConnectionPool(url, username, password, maxPoolSize);

            return pool;
        }

        public Connection getConnection() throws SQLException {
            if (availableConnections.isEmpty() && currentPoolSize < maxPoolSize) {
                // Create a new connection if the pool is empty, and we haven't reached the maximum pool size
                Connection connection = DriverManager.getConnection(url, username, password);
                availableConnections.add(connection);
                currentPoolSize++;
            }

            if (availableConnections.isEmpty()) {
                throw new SQLException("Connection pool is full and no connections are available");
            }

            // Move the first available connection from the available list to the used list
            Connection connection = availableConnections.remove(0);
            usedConnections.add(connection);
            return connection;
        }

        public void releaseConnection(Connection connection) {
            // Move the connection from the used list back to the available list
            usedConnections.remove(connection);
            availableConnections.add(connection);
        }

        public void closeAllConnections() throws SQLException {
            for (Connection connection : availableConnections) {
                connection.close();
                availableConnections.remove(connection);
            }
            for (Connection connection : usedConnections) {
                connection.close();
                usedConnections.remove(connection);
            }
        }
}


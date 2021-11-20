package eu.byncing.sql.lib;

import eu.byncing.scheduler.Scheduler;
import eu.byncing.sql.lib.table.Table;

import java.io.Closeable;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SqlLib implements Closeable {

    private Connection connection;

    private final Scheduler scheduler = new Scheduler();

    public void connect(Profile profile) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + profile.getHost() + ":" + profile.getPort() + "/" + profile.getDatabase(), profile.getUser(), profile.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (isConnected()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement query(String sql, boolean update) {
        try {
            if (!isConnected()) return null;
            PreparedStatement statement = connection.prepareStatement(sql);
            if (update) statement.executeUpdate();
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsTable(String table) {
        try {
            if (!isConnected()) return false;
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet result = metaData.getTables(null, null, table, null);
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Table table(String table) {
        return new Table(this, table);
    }

    public PreparedStatement table(String table, SqlKeys keys, DataTypes... types) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            builder.append(keys.getArray()[i]).append(" ").append(types[i].getValue());
            if (i != types.length - 1) builder.append(", ");
        }
        return query("CREATE TABLE IF NOT EXISTS " + table + "(" + builder + ")", true);
    }

    public void drop(String table) {
        if (!isConnected()) return;
        query("DROP TABLE " + table, true);
    }

    public void insert(String table, SqlKeys keys, SqlValues values) {
        try {
            if (!isConnected()) return;
            PreparedStatement statement = query("INSERT INTO " + table + "(" + keys + ") VALUES (" + keys.toValues() + ")", false);
            for (int i = 0; i < values.length(); i++) {
                statement.setObject(i + 1, values.getArray(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String table, SqlKeys keys, SqlValues values, SqlKeys wheresKeys, SqlValues wheresValues) {
        try {
            if (!isConnected()) return;
            String query = "UPDATE " + table + " SET " + keys.toSet();
            if (wheresKeys.length() > 0 && wheresValues.length() > 0) {
                query = query + " WHERE " + wheresKeys.toWhere();
            }
            PreparedStatement statement = query(query, false);
            int i1 = 0;
            for (int i = 0; i < values.length(); i++) {
                i1++;
                statement.setObject(i + 1, values.getArray(i));
            }
            if (wheresKeys.length() > 0 && wheresValues.length() > 0) {
                int i2 = i1;
                for (Object value : wheresValues.getArray()) {
                    i2++;
                    statement.setObject(i2, value);
                }
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String table, SqlKeys keys, SqlValues values) {
        update(table, keys, values, new SqlKeys(), new SqlValues());
    }

    public void remove(String table, SqlKeys where, SqlValues whereValues) {
        try {
            if (!isConnected()) return;
            PreparedStatement statement = query("DELETE FROM " + table + " WHERE " + where.toWhere(), false);
            for (int i = 0; i < whereValues.length(); i++) {
                statement.setObject(i + 1, whereValues.getArray(i));
            }
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Map<Integer, Map<String, Object>> select(String table, SqlKeys tableKeys, SqlKeys keys, SqlKeys where, SqlValues whereValues) {
        Map<Integer, Map<String, Object>> objects = new HashMap<>();
        try {
            if (!isConnected()) return objects;
            String query = "SELECT * FROM " + table;
            if (where.length() > 0 && whereValues.length() > 0) query = query + " WHERE " + where.toWhere();
            PreparedStatement statement = query(query, false);
            int count = 0;
            for (int i = 0; i < whereValues.length(); i++) {
                count++;
                statement.setObject(i + 1, whereValues.getArray(i));
            }
            ResultSet result = statement.executeQuery();
            int index = 0;
            while (result.next()) {
                index++;
                Map<String, Object> object = new HashMap<>();
                for (int i = 0; i < tableKeys.length(); i++) {
                    for (int i1 = 0; i1 < keys.length(); i1++) {
                        String s1 = keys.getArray(i1);
                        if (tableKeys.getArray(i).equals(s1)) object.put(s1, result.getObject(i + 1));
                    }
                }
                objects.put(index - 1, object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public boolean isConnected() {
        try {
            return (connection != null && !connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Connection getConnection() {
        return connection;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
package eu.byncing.sql.lib;

import eu.byncing.scheduler.Scheduler;
import eu.byncing.sql.lib.table.Table;

import java.io.Closeable;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public Table table(String table) {
        return new Table(this, table);
    }

    public PreparedStatement table(String table, SqlKeys keys, DataTypes... types) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            builder.append(keys.getKeys()[i]).append(" ").append(types[i].getValue());
            if (i != types.length - 1) builder.append(", ");
        }
        return query("CREATE TABLE IF NOT EXISTS " + table + "(" + builder + ")", true);
    }

    public void insert(String table, SqlKeys keys, SqlValues values) {
        try {
            if (!isConnected()) return;
            PreparedStatement statement = query("INSERT INTO " + table + "(" + keys + ") VALUES (" + keys.toValues() + ")", false);
            for (int i = 0; i < values.getValues().length; i++) {
                Object value = values.getValues()[i];
                if (value instanceof UUID) value = String.valueOf(value);
                statement.setObject(i + 1, value);
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
            if (wheresKeys.getKeys().length > 0 && wheresValues.getValues().length > 0) {
                query = query + " WHERE " + wheresKeys.toWhere();
            }

            PreparedStatement statement = query(query, false);
            int i1 = 0;
            for (int i = 0; i < values.getValues().length; i++) {
                i1++;
                Object value = values.getValues()[i];
                if (value instanceof UUID) value = String.valueOf(value);
                statement.setObject(i + 1, value);
            }

            if (wheresKeys.getKeys().length > 0 && wheresValues.getValues().length > 0) {
                int i2 = i1;
                for (Object value : wheresValues.getValues()) {
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

    public Map<Integer, Map<Integer, Object>> select(String table, int size) {
        Map<Integer, Map<Integer, Object>> values = new HashMap<>();
        try {
            if (!isConnected()) return values;
            PreparedStatement statement = query("SELECT * FROM " + table, false);
            ResultSet result = statement.executeQuery();
            int index = 0;
            while (result.next()) {
                Map<Integer, Object> objects = new HashMap<>();
                for (int i = 0; i < size; i++) objects.put(i, result.getObject(i + 1));
                values.put(index++, objects);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }

    public Map<String, Object> select(String table, SqlKeys keys, SqlKeys where, SqlValues whereValues) {
        Map<String, Object> objects = new HashMap<>();
        try {
            if (!isConnected()) return objects;
            PreparedStatement statement = query("SELECT * FROM " + table + " WHERE " + where.toWhere(), false);
            int count = 0;
            for (int i = 0; i < whereValues.getValues().length; i++) {
                count++;
                statement.setObject(i + 1, whereValues.getValues()[i]);
            }
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int index = count;
                for (String key : keys.getKeys()) {
                    index++;
                    objects.put(key, result.getObject(index));
                }
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
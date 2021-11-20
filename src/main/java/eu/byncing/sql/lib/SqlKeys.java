package eu.byncing.sql.lib;

public class SqlKeys {

    private final String[] keys;

    public SqlKeys(String... keys) {
        this.keys = keys;
    }

    public String[] getKeys() {
        return keys;
    }

    public String toValues() {
        String values = toString();
        for (String key : keys) values = values.replace(key, "?");
        return values;
    }

    public String toSet() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            builder.append(keys[i]).append(" = ").append("?");
            if (i != keys.length - 1) builder.append(", ");
        }
        return builder.toString();
    }

    public String toWhere() {
        return toSet().replace(", ", " AND ");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            builder.append(keys[i]);
            if (i != keys.length - 1) builder.append(", ");
        }
        return builder.toString();
    }
}
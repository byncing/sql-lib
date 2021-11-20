package eu.byncing.sql.lib;

public class SqlKeys {

    private final String[] array;

    public SqlKeys(String... keys) {
        this.array = keys;
    }

    public int length() {
        return array.length;
    }

    public String[] getArray() {
        return array;
    }

    public String getArray(int index) {
        return array[index];
    }

    public String toValues() {
        String values = toString();
        for (String key : array) values = values.replace(key, "?");
        return values;
    }

    public String toSet() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]).append(" = ").append("?");
            if (i != array.length - 1) builder.append(", ");
        }
        return builder.toString();
    }

    public String toWhere() {
        return toSet().replace(", ", " AND ");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i != array.length - 1) builder.append(", ");
        }
        return builder.toString();
    }
}
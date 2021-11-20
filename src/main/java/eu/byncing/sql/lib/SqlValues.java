package eu.byncing.sql.lib;

import java.util.UUID;

public class SqlValues {

    private final Object[] array;

    public SqlValues(Object... values) {
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            if (value instanceof UUID) values[i] = String.valueOf(value);
        }
        this.array = values;
    }

    public int length() {
        return array.length;
    }

    public Object[] getArray() {
        return array;
    }

    public Object getArray(int index) {
        return array[index];
    }
}
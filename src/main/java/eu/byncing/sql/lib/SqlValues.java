package eu.byncing.sql.lib;

public class SqlValues {

    private final Object[] values;

    public SqlValues(Object... values) {
        this.values = values;
    }

    public Object[] getValues() {
        return values;
    }
}
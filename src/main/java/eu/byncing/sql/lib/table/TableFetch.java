package eu.byncing.sql.lib.table;

import eu.byncing.sql.lib.SqlKeys;
import eu.byncing.sql.lib.SqlLib;
import eu.byncing.sql.lib.SqlValues;

import java.util.Map;

public class TableFetch {

    private final SqlLib lib;
    private final Table table;

    private SqlKeys where = new SqlKeys();
    private SqlValues whereValues = new SqlValues();

    public TableFetch(SqlLib lib, Table table) {
        this.lib = lib;
        this.table = table;
    }

    public boolean find(String key) {
        return single(key) != null;
    }

    public boolean find(String key, String value) {
        return lib.select(table.getTable(), table.getKeys(), new SqlKeys(), new SqlKeys(key), new SqlValues(value)).get(0) != null;
    }

    public <T> T single(String key, Class<T> clazz) {
        Map<String, Object> select = lib.select(table.getTable(), table.getKeys(), new SqlKeys(key), where, whereValues).get(0);
        return (T) select.get(key);
    }

    public Map<String, Object> single(String... keys) {
        return lib.select(table.getTable(), table.getKeys(), new SqlKeys(keys), where, whereValues).get(0);
    }

    public Map<Integer, Map<String, Object>> all(String... keys) {
        return lib.select(table.getTable(), table.getKeys(), new SqlKeys(keys), where, whereValues);
    }

    public TableFetch release() {
        where = new SqlKeys();
        whereValues = new SqlValues();
        return this;
    }

    public TableFetch setWhere(String... where) {
        this.where = new SqlKeys(where);
        return this;
    }

    public TableFetch setWhereValues(Object... whereValues) {
        this.whereValues = new SqlValues(whereValues);
        return this;
    }
}
package eu.byncing.sql.lib.table;

import eu.byncing.sql.lib.SqlKeys;
import eu.byncing.sql.lib.SqlLib;
import eu.byncing.sql.lib.SqlValues;

public class TableUpdate {

    private final SqlLib lib;

    private final Table table;

    private SqlKeys keys;

    private SqlKeys wheres = new SqlKeys();
    private SqlValues wheresValues = new SqlValues();

    public TableUpdate(SqlLib lib, Table table, SqlKeys keys) {
        this.lib = lib;
        this.table = table;
        this.keys = keys;
    }

    public TableUpdate(SqlLib lib, Table table) {
        this(lib, table, new SqlKeys());
    }

    public void invoke(Object... objects) {
        lib.update(table.getTable(), keys, new SqlValues(objects), wheres, wheresValues);
    }

    public void invoke(String key, Object value) {
        lib.update(table.getTable(), new SqlKeys(key), new SqlValues(value), new SqlKeys(), new SqlValues());
    }

    public void invoke(String wheres, Object wheresValues, String key, Object value) {
        lib.update(table.getTable(), new SqlKeys(key), new SqlValues(value), new SqlKeys(wheres), new SqlValues(wheresValues));
    }

    public SqlKeys getKeys() {
        return keys;
    }

    public TableUpdate setKeys(String... keys) {
        this.keys = new SqlKeys(keys);
        return this;
    }

    public SqlKeys getWheres() {
        return wheres;
    }

    public TableUpdate setWhere(String... wheres) {
        this.wheres = new SqlKeys(wheres);
        return this;
    }

    public SqlValues getWheresValues() {
        return wheresValues;
    }

    public TableUpdate setWhereValues(Object... objects) {
        this.wheresValues = new SqlValues(objects);
        return this;
    }
}
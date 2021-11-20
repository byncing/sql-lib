package eu.byncing.sql.lib.table;

import eu.byncing.sql.lib.SqlKeys;
import eu.byncing.sql.lib.SqlLib;
import eu.byncing.sql.lib.SqlValues;

public class TableUpdate {

    private final SqlLib lib;

    private final Table table;

    private SqlKeys keys;

    private SqlKeys where = new SqlKeys();
    private SqlValues whereValues = new SqlValues();

    public TableUpdate(SqlLib lib, Table table, SqlKeys keys) {
        this.lib = lib;
        this.table = table;
        this.keys = keys;
    }

    public TableUpdate(SqlLib lib, Table table) {
        this(lib, table, new SqlKeys());
    }

    public void change(Object... objects) {
        lib.update(table.getTable(), keys, new SqlValues(objects), where, whereValues);
    }

    public void change(String key, Object value) {
        lib.update(table.getTable(), new SqlKeys(key), new SqlValues(value), new SqlKeys(), new SqlValues());
    }

    public void change(String wheres, Object wheresValues, String key, Object value) {
        lib.update(table.getTable(), new SqlKeys(key), new SqlValues(value), new SqlKeys(wheres), new SqlValues(wheresValues));
    }

    public void delete() {
        lib.remove(table.getTable(), where, whereValues);
    }

    public void delete(String where, Object whereValue) {
        lib.remove(table.getTable(), new SqlKeys(where), new SqlValues(whereValue));
    }

    public SqlKeys getKeys() {
        return keys;
    }

    public TableUpdate setKeys(String... keys) {
        this.keys = new SqlKeys(keys);
        return this;
    }

    public SqlKeys getWhere() {
        return where;
    }

    public TableUpdate setWhere(String... wheres) {
        this.where = new SqlKeys(wheres);
        return this;
    }

    public SqlValues getWhereValues() {
        return whereValues;
    }

    public TableUpdate setWhereValues(Object... objects) {
        this.whereValues = new SqlValues(objects);
        return this;
    }
}
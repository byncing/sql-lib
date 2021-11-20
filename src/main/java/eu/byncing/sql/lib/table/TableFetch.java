package eu.byncing.sql.lib.table;

import eu.byncing.sql.lib.SqlKeys;
import eu.byncing.sql.lib.SqlLib;
import eu.byncing.sql.lib.SqlValues;

import java.util.Map;

public class TableFetch {

    private final SqlLib lib;
    private final Table table;

    private SqlKeys keys = new SqlKeys();
    private SqlValues values = new SqlValues();

    public TableFetch(SqlLib lib, Table table) {
        this.lib = lib;
        this.table = table;
    }

    public Map<String, Object> invoke(String... keys) {
        return lib.select(table.getTable(), new SqlKeys(keys), this.keys, this.values);
    }

    public TableFetch setKeys(String... keys) {
        this.keys = new SqlKeys(keys);
        return this;
    }

    public TableFetch setValues(Object... values) {
        this.values = new SqlValues(values);
        return this;
    }
}
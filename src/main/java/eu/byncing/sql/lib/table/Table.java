package eu.byncing.sql.lib.table;

import eu.byncing.sql.lib.SqlKeys;
import eu.byncing.sql.lib.SqlLib;
import eu.byncing.sql.lib.DataTypes;
import eu.byncing.sql.lib.SqlValues;

public class Table {
    private final SqlLib lib;

    private final String table;

    private SqlKeys keys = new SqlKeys();
    private DataTypes[] types = new DataTypes[]{};

    public Table(SqlLib lib, String table) {
        this.lib = lib;
        this.table = table;
    }

    public void createTable() {
        lib.table(table, keys, types);
    }

    public void insert(Object... objects) {
        lib.insert(table, keys, new SqlValues(objects));
    }

    public TableUpdate update() {
        return new TableUpdate(lib, this);
    }

    public TableFetch fetch() {
        return new TableFetch(lib, this);
    }

    public String getTable() {
        return table;
    }

    public SqlKeys getKeys() {
        return keys;
    }

    public Table setKeys(String... keys) {
        this.keys = new SqlKeys(keys);
        return this;
    }

    public DataTypes[] getTypes() {
        return types;
    }

    public Table setTypes(DataTypes... types) {
        this.types = types;
        return this;
    }
}
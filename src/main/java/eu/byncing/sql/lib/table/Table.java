package eu.byncing.sql.lib.table;

import eu.byncing.sql.lib.DataTypes;
import eu.byncing.sql.lib.SqlKeys;
import eu.byncing.sql.lib.SqlLib;
import eu.byncing.sql.lib.SqlValues;

import java.util.function.Consumer;

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

    public void deleteTable() {
        lib.drop(table);
    }

    public void insert(Object... objects) {
        lib.insert(table, keys, new SqlValues(objects));
    }

    public void update(Consumer<TableUpdate> consumer, boolean async) {
        TableUpdate update = new TableUpdate(lib, this);
        if (async) lib.getScheduler().runAsync(() -> consumer.accept(update));
        else consumer.accept(update);
    }

    public void fetch(Consumer<TableFetch> consumer, boolean async) {
        TableFetch fetch = new TableFetch(lib, this);
        if (async) lib.getScheduler().runAsync(() -> consumer.accept(fetch));
        else consumer.accept(fetch);
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
package eu.byncing.sql.lib;

public enum DataTypes {

    STRING("VARCHAR(255)"),
    INTEGER("INT"),
    BOOLEAN("BOOL"),
    FLOAT("REAL"),
    DOUBLE("DOUBLE"),
    LONG("BIGINT"),
    BYTE("TINYINT");

    private final String value;

    DataTypes(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
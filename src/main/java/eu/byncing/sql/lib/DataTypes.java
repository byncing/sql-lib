package eu.byncing.sql.lib;

public enum DataTypes {

    STRING("VARCHAR(255)"),
    INTEGER("INT"),
    BOOLEAN("BOOL"),
    FLOAT("FLOAT"),
    DOUBLE("DOUBLE"),
    LONG("LONG");

    private final String value;

    DataTypes(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
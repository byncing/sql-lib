package eu.byncing.sql.lib;

public class Profile {

    private final String host;
    private final int port;
    private final String user, database, password;

    public Profile(String host, int port, String user, String database, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.database = database;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getDatabase() {
        return database;
    }

    public String getPassword() {
        return password;
    }
}
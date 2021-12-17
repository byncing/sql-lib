# sql-lib
A simple sql library that doesn't require you to write a single line of sql

# Gradle
````gradle
repositories {
    maven {
        url('http://repo.byncing.eu/snapshots')
        allowInsecureProtocol(true)
    }
}

dependencies {
    implementation('eu.byncing:sql-lib:2.0.3-SNAPSHOT')
}
````
&nbsp;
&nbsp;
# Example
````java
package eu.byncing.sql.test;

import eu.byncing.sql.lib.DataTypes;
import eu.byncing.sql.lib.Profile;
import eu.byncing.sql.lib.SqlLib;
import Table;

import java.util.Map;
import java.util.UUID;

public class SqlTest {

    public static void main(String[] args) {
        SqlLib lib = new SqlLib();
        lib.connect(new Profile("127.0.0.1", 3306, "root", "bridge", ""));

        Table table = lib.table("players");
        table.setKeys("UUID", "NAME", "CREATE_TIME", "ONLINE");
        table.setTypes(DataTypes.STRING, DataTypes.STRING, DataTypes.LONG, DataTypes.BOOLEAN);
        table.createTable();

        PlayerData data = new PlayerData(UUID.randomUUID(), "byncing", System.currentTimeMillis(), true);

        table.fetch(fetch -> {
            if (!fetch.find("NAME", "byncing")) {
                table.insert(data.serialize());
            }
            fetch.setWhere("NAME").setWhereValues("byncing");
            PlayerData playerData = PlayerData.deserialize(fetch.single("UUID", "NAME", "CREATE_TIME", "ONLINE"));
            System.out.println(playerData);
        }, true);

        table.update(update -> {
            update.setWhere("NAME").setWhereValues("byncing");
            table.fetch(fetch -> {
                fetch.setWhere("NAME").setWhereValues("byncing");
                update.setKeys("ONLINE").change(!fetch.single("ONLINE", Boolean.class));
            }, false);
        }, true);
    }

    public static class PlayerData {

        private final UUID uniqueId;
        private final String name;
        private final long createTime;
        private boolean online;

        public PlayerData(UUID uniqueId, String name, long createTime, boolean online) {
            this.uniqueId = uniqueId;
            this.name = name;
            this.createTime = createTime;
            this.online = online;
        }

        public Object[] serialize() {
            return new Object[]{uniqueId, name, createTime, online};
        }

        public static PlayerData deserialize(Map<String, Object> values) {
            return new PlayerData(
                    UUID.fromString((String) values.get("UUID")),
                    ((String) values.get("NAME")),
                    ((Long) values.get("CREATE_TIME")),
                    ((Boolean) values.get("ONLINE")));
        }

        public UUID getUniqueId() {
            return uniqueId;
        }

        public String getName() {
            return name;
        }

        public long getCreateTime() {
            return createTime;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        @Override
        public String toString() {
            return "PlayerData{" +
                    "uniqueId=" + uniqueId +
                    ", name='" + name + '\'' +
                    ", createTime=" + createTime +
                    ", online=" + online +
                    '}';
        }
    }
}
````

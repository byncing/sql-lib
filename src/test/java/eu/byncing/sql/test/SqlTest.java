package eu.byncing.sql.test;

import eu.byncing.sql.lib.DataTypes;
import eu.byncing.sql.lib.Profile;
import eu.byncing.sql.lib.SqlLib;
import eu.byncing.sql.lib.table.Table;
import eu.byncing.sql.lib.table.TableFetch;
import eu.byncing.sql.lib.table.TableUpdate;

import java.util.Map;
import java.util.UUID;

public class SqlTest {

    public static void main(String[] args) throws ClassNotFoundException {
        SqlLib lib = new SqlLib();
        lib.connect(new Profile("127.0.0.1", 3306, "root", "bridge", ""));

        //Create a table with types
        Table table = lib.table("players");
        table.setKeys("UNIQUE_ID", "NAME", "ONLINE").
                setTypes(DataTypes.STRING, DataTypes.STRING, DataTypes.BOOLEAN).
                createTable();

        //Added of the table
        UUID uniqueId = UUID.randomUUID();
        table.insert(uniqueId.toString(), "byncing", true);

        //Update a table with a key
        TableUpdate update = table.update();
        update.setWhere("UNIQUE_ID").setWhereValues(uniqueId.toString());

        update.setKeys("ONLINE").invoke(false);

        //Fetch of a table with key
        TableFetch fetch = table.fetch();
        fetch.setKeys("UNIQUE_ID").setValues(uniqueId.toString());

        Map<String, Object> values = fetch.invoke("NAME", "ONLINE");
        values.forEach((s, o) -> System.out.println("key " + s + ", value " + o));
    }
}
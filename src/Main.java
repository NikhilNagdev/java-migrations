
import database.CRUD;
import database.Database;
import database.migrations.MigrationCreator;
import database.migrations.Migrator;
import database.querybuilder.QueryBuilder;
import parser.Parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        MigrationCreator mc = new MigrationCreator();
        mc.createMigration("create", "users");
        mc.createMigration("create", "questions");

        Migrator migrator = new Migrator();
        migrator.runMigrations();
//
//        QueryBuilder qb = new QueryBuilder("migrations", new CRUD(new Parser("database").getDatabase()));
//        List<LinkedHashMap<String, Object>> values = new ArrayList<>();
//        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
//        map.put("id", "1");
//        map.put("name", "1");
//        LinkedHashMap<String, Object> map2 = new LinkedHashMap<>();
//        map2.put("id", "2");
//        map2.put("name", "@");
//        values.add(map);
//        values.add(map2);
//        System.out.println(qb.insert(values));

//        System.out.println(qb
//                .select("*")
//                .join("questions", "questions.user_id", "users.id")
//                .where("users.id", 1)
//                .get()
//        );
    }

    private Parser p = new Parser("database");
}

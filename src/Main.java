
import database.CRUD;
import database.Database;
import database.migrations.MigrationCreator;
import database.migrations.Migrator;
import database.querybuilder.QueryBuilder;
import parser.Parser;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

//        MigrationCreator mc = new MigrationCreator();
//        mc.createMigration("create", "users");
//        mc.createMigration("create", "questions");
//
//        Migrator migrator = new Migrator();
//        migrator.runMigrations();

        QueryBuilder qb = new QueryBuilder("users", new CRUD(new Parser("database").getDatabase()));
        System.out.println(qb
                .select("*")
                .join("roles", "roles.id", "users.id")
                .leftJoin("roles", "roles.id", "=", "users.id")
                .where("id", "1")
                .get()
        );
    }

    private Parser p = new Parser("database");
}

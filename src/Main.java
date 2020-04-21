
import database.Database;
import database.migrations.MigrationCreator;
import database.migrations.Migrator;
import parser.Parser;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        MigrationCreator mc = new MigrationCreator();
        mc.createMigration("create", "users");
        mc.createMigration("create", "questions");

        Migrator migrator = new Migrator();
        migrator.runMigrations();
    }

    public void setConfigAttributes(){
        Database db = p.getDatabase();
        System.out.println(db.getConnection());
    }

    private Parser p = new Parser("database");

}

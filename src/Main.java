
import database.CRUD;
import database.Database;
import database.Table;
import database.querybuilder.QueryBuilder;
import files.FileOperation;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

//        File folder = new File("database");
//        System.out.println(folder.getCanonicalPath());
//        /Creating a File object for directory
//        File directoryPath = new File("D:\\ExampleDirectory");
//        //List of all files and directories
//        String contents[] = directoryPath.list();
//        System.out.println("List of files and directories in the specified directory:");
//        for(int i=0; i<contents.length; i++) {
//            System.out.println(contents[i]);
//        new Main().setConfigAttributes();

        System.out.println(new FileOperation().getAllPathsMigration("database\\migrations"));



//        System.out.println(p.getTables());
        new Main().generateDatabaseTable();


//        CRUD.table("users")
//                .select("id", "name")
//                .where("id", 1)
//                .andWhere("name", "nikhil")
//                .orWhere("id", 2)
//                .andWhere("name", "nagdev")
//                .get();


    }

    public void generateDatabaseTable(){
        Parser p = new Parser("database");
        QueryBuilder qb = new QueryBuilder("", null);
        List<Table> tables = p.getTables();
        for(Table table : tables){
            System.out.println(qb.generateTableQuery(table));
            System.out.println("================================================================");
        }

    }

    public void setConfigAttributes(){
        Database db = p.getDatabase();
        System.out.println(db.getConnection());
    }

    private Parser p = new Parser("database");

}

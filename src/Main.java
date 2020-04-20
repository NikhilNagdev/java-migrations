
import database.CRUD;
import database.Database;
import database.Table;
import database.querybuilder.QueryBuilder;
import files.FileOperation;
import parser.Parser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
        System.out.println(timeStamp);

        FileOperation f = new FileOperation();
        f.createMigrationFile("questions", "create");


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

//
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
        CRUD crud = new CRUD(p.getDatabase());
        int i = 0;
        for(Table table : tables){
            i++;
            System.out.println(qb.generateTableQuery(table));
            if(crud.runCreate(qb.generateTableQuery(table)))
                System.out.println("Table created");
            else
                System.out.println("There was some problem while creating table");
            System.out.println();
        }
        System.out.println(i);

    }

    public void setConfigAttributes(){
        Database db = p.getDatabase();
        System.out.println(db.getConnection());
    }

    private Parser p = new Parser("database");

}

package files;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Files;

public class FileOperation {

    public List<FileInputStream> getListOfFileInputStream(List<String> paths){

        List<FileInputStream> migrationFileInputStreams = new ArrayList<FileInputStream>();
        for(String path : paths){
            migrationFileInputStreams.add(getFileInputStream(path));
        }
        return migrationFileInputStreams;
    }

    public FileInputStream getFileInputStream(String path){

        try{
            return new FileInputStream(path);
        }catch(IOException ie){
            System.out.println("Exception while generating FileInputStream: " + ie);
        }

        return null;
    }

    public List<String> getAllPathsMigration(String folderPath){
        File folder = new File(folderPath);
        List<String> paths = new ArrayList<String>();
        try {
            System.out.println(folder.getCanonicalPath());
        } catch (IOException e) {
            System.out.println("Exception while doing operation with files " + e);
        }
        File[] listOfFiles = folder.listFiles();

        getFilesSortedByFIleName(listOfFiles);

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                paths.add(folderPath + File.separator + listOfFile.getName());
                System.out.println(listOfFile.getName());
                noOfTables++;
            }
        }
        return paths;
    }

    private void getFilesSortedByFIleName(File files[]){
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(final File f1, final File f2) {

                return f1.getName().compareTo(f2.getName());

//                try {
//                    BasicFileAttributes f1Attr = Files.readAttributes(Paths.get(f1.toURI()), BasicFileAttributes.class);
//                    BasicFileAttributes f2Attr = Files.readAttributes(Paths.get(f2.toURI()), BasicFileAttributes.class);
//                    return f1Attr.creationTime().compareTo(f2Attr.creationTime());
//                } catch (IOException e) {
//                    return 0;
//                }
            }
        });
    }

    public void createMigrationFile(String tableName, String type){

//        File myObj = new File("database\\migrations\\" + this.getCurrentTimestamp() + "_" + type + "_" + tableName + ".json");
        try {
            FileWriter myWriter = new FileWriter("database\\migrations\\" + this.getCurrentTimestamp() + "_" + type + "_" + tableName + ".json");
            myWriter.write(constants.Files.CREATE_TABLE_MIGRATION_STRUCTURE);
            System.out.println(constants.Files.CREATE_TABLE_MIGRATION_STRUCTURE);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            if (myObj.createNewFile()) {
//                System.out.println("File created: " + myObj.getName());
//            } else {
//                System.out.println("File already exists.");
//            }
//        } catch (IOException e) {
//            System.out.println("Exception while creating migration file: " + e);
//        }

    }

    private String getCurrentTimestamp(){
        return new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
    }



    public int getNoOfTables(){
        return noOfTables;
    }

    private int noOfTables = 0;


}

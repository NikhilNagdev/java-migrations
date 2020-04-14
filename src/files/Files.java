package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Files {

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

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                paths.add(folderPath + File.separator + listOfFile.getName());
                System.out.println(listOfFile.getName());
                noOfTables++;
            }
        }
        return paths;
    }

    public int getNoOfTables(){
        return noOfTables;
    }

    private int noOfTables = 0;


}

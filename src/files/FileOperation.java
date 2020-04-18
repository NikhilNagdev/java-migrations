package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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

        getFilesSortedByCreationDate(listOfFiles);

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                paths.add(folderPath + File.separator + listOfFile.getName());
                System.out.println(listOfFile.getName());
                noOfTables++;
            }
        }
        return paths;
    }

    private void getFilesSortedByCreationDate(File files[]){
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(final File f1, final File f2) {
                try {
                    BasicFileAttributes f1Attr = Files.readAttributes(Paths.get(f1.toURI()), BasicFileAttributes.class);
                    BasicFileAttributes f2Attr = Files.readAttributes(Paths.get(f2.toURI()), BasicFileAttributes.class);
                    return f1Attr.creationTime().compareTo(f2Attr.creationTime());
                } catch (IOException e) {
                    return 0;
                }
            }
        });
    }



    public int getNoOfTables(){
        return noOfTables;
    }

    private int noOfTables = 0;


}

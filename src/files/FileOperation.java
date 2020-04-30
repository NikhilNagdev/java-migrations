package files;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        File[] listOfFiles = folder.listFiles();

        getFilesSortedByFIleName(listOfFiles);

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                paths.add(folderPath + File.separator + listOfFile.getName());
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
            }
        });
    }

    public File[] getFilesFromFolder(String folderPath){
        return new File(folderPath).listFiles();
    }

    public List<String> getFileNamesFromFolder(String folderPath){
        File folder = new File(folderPath);
        List<String> fileNames = new ArrayList<String>();
        File[] files = folder.listFiles();
        getFilesSortedByFIleName(files);
        for(File file : files){
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    public int getNoOfTables(){
        return noOfTables;
    }

    public void createFileWithContent(String path, String name, String content){
        try {
            FileWriter myWriter = new FileWriter(path + "\\" + name);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int noOfTables = 0;


}

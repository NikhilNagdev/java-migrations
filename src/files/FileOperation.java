package files;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperation {

    /**
     * This method returns a new FileInputStream of a path
     * @param path
     * @return
     */
    public FileInputStream getFileInputStream(String path){
        try{
            return new FileInputStream(path);
        }catch(IOException ie){
            System.out.println("Exception while generating FileInputStream: " + ie);
        }
        return null;
    }

    /**
     * This method is used to get the paths of migration file in the folder
     * @param folderPath folder from which the files are to be get
     * @return List of file paths
     */
    public List<String> getAllPathsMigration(String folderPath){
        File folder = new File(folderPath);
        List<String> paths = new ArrayList<String>();
        File[] listOfFiles = folder.listFiles();
        getFilesSortedByFIleName(listOfFiles);
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {//folders should not be included
                paths.add(folderPath + File.separator + listOfFile.getName());
            }
        }
        return paths;
    }

    /**
     * This method is used to sort the files according to the file name.
     * It overrides the compare methods to sort the files according to the name/
     * @param files files to be sorted
     */
    private void getFilesSortedByFIleName(File files[]){
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(final File f1, final File f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
    }

    /**
     * This method is used to get all the file names from the folder.
     * @param folderPath folder from which the file names has to be get.
     * @return List of filenames
     */
    public List<String> getFileNamesFromFolder(String folderPath){
        File folder = new File(folderPath);
        List<String> fileNames = new ArrayList<String>();
        File[] files = folder.listFiles();//getting all Files in the folder
        getFilesSortedByFIleName(files);
        for(File file : files){
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    /**
     * This method is used to create a file in the specified folder with some content written in file.
     * @param path path in which file is to be created
     * @param name name of the file to be created
     * @param content content that is to be written on file
     */
    public void createFileWithContent(String path, String name, String content){
        try {
            FileWriter myWriter = new FileWriter(path + "\\" + name);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Exception while creating a file " + e);
        }
    }
}

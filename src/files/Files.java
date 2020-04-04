package files;

import java.io.IOException;
import java.io.FileInputStream;

public class Files {

    public static FileInputStream getFileInputStream(String path){

        try{
            return new FileInputStream(path);
        }catch(IOException ie){
            System.out.println("Error while reading file: " + ie);
        }

        return null;
    }

}

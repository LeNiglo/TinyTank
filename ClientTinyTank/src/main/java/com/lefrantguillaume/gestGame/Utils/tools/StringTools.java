package com.lefrantguillaume.gestGame.Utils.tools;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by andres_k on 24/03/2015.
 */

public class StringTools {

    static public String readFile(String filename)
    {
        String content = null;
        File file = new File(filename); //for ex foo.txt
        Debug.debug("file: " +file.getAbsolutePath());
        try {
            FileReader reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            content = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

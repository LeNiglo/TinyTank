package com.lefrantguillaume.utils.tools;

import java.io.*;

/**
 * Created by andres_k on 24/03/2015.
 */

public class StringTools {

    public static String readFile(String fileName) {
        String content = "";
        File file = new File(fileName); //for ex foo.txt
        Debug.debug("file: " + file.getAbsolutePath());
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

    public static void writeInFile(String fileName, String value) {
        File file = new File(fileName);

        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(value);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String duplicateString(String value, int number){
        String result = "";

        for (int i = 0; i < number; ++i){
            result += value;
        }
        return result;
    }

    public static float charSizeX(){
        return 9.2f;
    }

    public static float charSizeY(){
        return 20f;
    }
}

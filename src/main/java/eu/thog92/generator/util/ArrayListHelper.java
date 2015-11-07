package eu.thog92.generator.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ArrayListHelper
{

    public static ArrayList<String> loadStringArrayFromFile(String file)
            throws IOException
    {

        ArrayList<String> tmp = new ArrayList<>();
        BufferedReader fileIn = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));

        String entry;

        while ((entry = fileIn.readLine()) != null)
        {
            tmp.add(entry);
        }
        fileIn.close();
        return tmp;
    }
}

package eu.thog92.generator.api;

import eu.thog92.generator.util.ArrayListHelper;
import eu.thog92.generator.util.WritableArrayList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary
{
    private HashMap<String, ArrayList<String>> dictionary;
    private File dicDir;


    public void loadCombinations() throws IOException
    {
        if (!dicDir.exists() || dicDir.listFiles() == null)
            throw new IOException(dicDir.getAbsolutePath() + " directory doesn't exist!");

        this.dictionary = new HashMap<>();
        System.out.println("Loading Files...");
        for (String file : dicDir.list())
        {
            dictionary.put(
                    file.replace(".txt", ""),
                    ArrayListHelper.loadStringArrayFromFile(dicDir
                            .getAbsolutePath() + File.separator + file));
        }
    }

    public ArrayList<String> get(String target)
    {
        return dictionary.get(target);
    }

    public void setDir(File dir)
    {
        this.dicDir = dir;
    }

    public void loadBlackList() throws IOException
    {
        File blackListFile = new File(dicDir.getParentFile(), "blacklist.txt");
        if (!blackListFile.exists())
            blackListFile.createNewFile();

        dictionary.put(blackListFile.getName().replace(".txt", ""), new WritableArrayList<>(ArrayListHelper.loadStringArrayFromFile(blackListFile.getAbsolutePath()), blackListFile));
    }
}

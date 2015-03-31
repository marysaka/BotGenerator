package eu.thog92.generator.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Configuration
{
    private final File configFile;
    private final Gson gson;

    public Configuration(File baseDir, String name)
    {
        this(baseDir, name, name.toLowerCase());
    }

    public Configuration(File baseDir, String configDir, String configName)
    {
        this.configFile = new File(baseDir, configDir + File.separator + configName.toLowerCase() + ".json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    public <T> T readFromFile(Class<T> clazz)
    {
        try
        {
            return this.gson.fromJson(new FileReader(configFile), clazz);
        } catch (FileNotFoundException e)
        {
            return null;
        }
    }

    public <T> void saveToDisk(T config)
    {
        try
        {
            if (!configFile.exists())
            {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(configFile);
            fileOutputStream.write(this.gson.toJson(config).getBytes());
            fileOutputStream.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}

package eu.thog92.generator;

import com.esotericsoftware.yamlbeans.YamlReader;
import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.api.Module;
import eu.thog92.generator.core.Config;
import eu.thog92.generator.core.TasksManager;
import eu.thog92.generator.core.loader.AnnotationFinder;
import eu.thog92.generator.core.tasks.GeneratorTask;
import eu.thog92.generator.core.tasks.TwitterTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BotGeneratorImpl extends BotGenerator
{
    private TwitterTask drama;
    private GeneratorTask generatorTask;

    protected BotGeneratorImpl() throws IllegalAccessException, IOException
    {
        super(new TasksManager());
    }

    public static void main(String[] args)
    {
        try
        {
            new BotGeneratorImpl().init();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void init()
    {
        super.init();
    }

    @Override
    protected Config readConfigFile() throws IOException
    {
        File configFile = new File("config.yml");
        if (!configFile.exists())
        {
            throw new FileNotFoundException("Config not found");
        }
        YamlReader reader = new YamlReader(new FileReader(configFile));
        reader.close();
        return config;
    }
}

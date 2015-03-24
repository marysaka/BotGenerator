package eu.thog92.generator.api;

import eu.thog92.generator.api.tasks.ITaskManager;
import eu.thog92.generator.core.Config;
import eu.thog92.generator.core.http.HttpServerManager;
import eu.thog92.generator.core.loader.AnnotationFinder;

import java.io.IOException;
import java.util.List;

public abstract class BotGenerator
{
    private static BotGenerator instance;
    protected ITaskManager tasksManager;
    protected Config config;
    protected HttpServerManager httpServerManager;

    protected BotGenerator(ITaskManager tasksManager) throws IllegalAccessException
    {
        if(instance != null)
            throw new IllegalAccessException("The bot is already instanced!");

        instance = this;
        this.tasksManager = tasksManager;
    }


    public void init()
    {
        try
        {
            // Init External Modules
            long startTime = System.currentTimeMillis();
            AnnotationFinder loader = new AnnotationFinder();
            List<Class> modules = loader.search(Module.class);

            System.out.println((System.currentTimeMillis() - startTime) + "ms");
            this.config = this.readConfigFile();
            this.tasksManager.setConfig(config);
            //this.tasksManager = new TasksManager();
            /*this.generatorTask = new GeneratorTask();
            drama = new TwitterTask(generatorTask);
            drama.setDelay(config.delay);
            this.tasksManager.scheduleTask(drama);*/

            if (config.isHTTPSeverEnabled)
                this.httpServerManager = new HttpServerManager(this);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("INIT Failed! EXITING...");
            System.exit(-1);
        }
    }

    protected abstract Config readConfigFile() throws IOException;


    public static BotGenerator getInstance()
    {
        return instance;
    }

    public ITaskManager getTasksManager()
    {
        return tasksManager;
    }

    public Config getConfig()
    {
        return config;
    }

    public void reload()
    {
        try
        {
            this.tasksManager.reload();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

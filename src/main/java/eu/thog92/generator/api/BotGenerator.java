package eu.thog92.generator.api;

import eu.thog92.generator.api.events.EventBus;
import eu.thog92.generator.api.tasks.ITaskManager;
import eu.thog92.generator.core.Config;
import eu.thog92.generator.core.http.HttpServerManager;

import java.io.IOException;

public abstract class BotGenerator
{
    private static BotGenerator instance;
    protected ITaskManager tasksManager;
    protected EventBus eventBus;
    protected Config config;
    protected HttpServerManager httpServerManager;

    protected BotGenerator(ITaskManager tasksManager, EventBus eventBus) throws IllegalAccessException
    {
        if(instance != null)
            throw new IllegalAccessException("The bot is already instanced!");

        instance = this;
        this.tasksManager = tasksManager;
        this.eventBus = eventBus;
    }


    public void init()
    {
        try
        {

            this.config = this.readConfigFile();
            this.tasksManager.setConfig(config);

            // Init External Modules
            this.initModules();

            if (config.isHTTPSeverEnabled)
                this.httpServerManager = new HttpServerManager(this);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("INIT Failed! EXITING...");
            System.exit(-1);
        }
    }

    protected abstract void initModules();

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

    public EventBus getEventBus()
    {
        return eventBus;
    }
}

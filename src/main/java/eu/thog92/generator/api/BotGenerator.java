package eu.thog92.generator.api;

import eu.thog92.generator.api.events.EventBus;
import eu.thog92.generator.api.irc.IRCConfiguration;
import eu.thog92.generator.api.tasks.ITaskManager;

import java.io.IOException;

public abstract class BotGenerator
{
    private static BotGenerator instance;
    protected ITaskManager tasksManager;
    protected EventBus eventBus;
    protected IHttpServerManager httpServerManager;

    protected BotGenerator(ITaskManager tasksManager, EventBus eventBus, IHttpServerManager httpServerManager) throws IllegalAccessException
    {
        if (instance != null)
            throw new IllegalAccessException("The bot is already instanced!");

        instance = this;
        this.tasksManager = tasksManager;
        this.eventBus = eventBus;
        this.httpServerManager = httpServerManager;
    }

    public static BotGenerator getInstance()
    {
        return instance;
    }

    public void init()
    {
        try
        {


            // Init External Modules
            this.initModules();

        } catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("INIT Failed! EXITING...");
            System.exit(-1);
        }
    }

    protected abstract void initModules();

    public ITaskManager getTasksManager()
    {
        return tasksManager;
    }


    public EventBus getEventBus()
    {
        return eventBus;
    }

    public IHttpServerManager getHttpManager()
    {
        return httpServerManager;
    }

    public String getAPIVersion()
    {
        return "1.3";
    }

    public abstract void checkAndCreateIRCClient(IRCConfiguration ircConfiguration, String logFileName) throws IOException;
}

package eu.thog92.generator;

import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.api.annotations.Module;
import eu.thog92.generator.api.events.EventBus;
import eu.thog92.generator.api.events.InitEvent;
import eu.thog92.generator.api.irc.IRCClient;
import eu.thog92.generator.api.irc.IRCConfiguration;
import eu.thog92.generator.core.TasksManager;
import eu.thog92.generator.core.exception.ModuleInitializationException;
import eu.thog92.generator.core.http.HttpServerManager;
import eu.thog92.generator.core.loader.ModuleFinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotGeneratorImpl extends BotGenerator
{
    private final List<String> activesAddons = new ArrayList<>();
    private ModuleFinder loader;

    private BotGeneratorImpl() throws IllegalAccessException
    {
        super(new TasksManager(), new EventBus(), new HttpServerManager());
    }

    public static void main(String[] args)
    {
        try
        {
            new BotGeneratorImpl().init();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void initModules()
    {
        loader = new ModuleFinder();
        Map<String, Class> modules = loader.search();
        System.out.println("Found " + modules.size() + " modules");
        eventBus = new EventBus();
        for (String name : modules.keySet())
        {
            try
            {
                this.loadModule(modules, name);
            } catch (ModuleInitializationException e)
            {
                e.printStackTrace();
            }
        }

        this.eventBus.post(new InitEvent());
        System.out.println("Active modules: " + activesAddons.size() + " " +  modules.keySet());
    }

    @Override
    public void checkAndCreateIRCClient(IRCConfiguration ircConfiguration, String logDirName) throws IOException
    {
        if(ircConfiguration.enabled)
        {
            File logFile = new File("log/" + logDirName + "/irc.log");
            logFile.getParentFile().mkdirs();
            logFile.delete();
            logFile.createNewFile();

            FileOutputStream out = new FileOutputStream(logFile);
            IRCClient ircClient = IRCClient.createIRCClient(ircConfiguration.hostname, ircConfiguration.port, ircConfiguration.username).addChannels(ircConfiguration.channels).setPrintStream(new PrintStream(out, false, "UTF-8"));
            ircClient.connect();
        }
    }

    private void loadModule(Map<String, Class> modules, String name) throws ModuleInitializationException
    {
        if (activesAddons.contains(name) || name.equals(" ") || name.isEmpty()) return;

        Module annot = loader.getAnnotFromClass(name);
        if (annot == null)
            throw new ModuleInitializationException(name + " not found!");

        if (!annot.dependencies().isEmpty())
        {
            for (String dependency : annot.dependencies().split("after:"))
            {
                this.loadModule(modules, dependency.replace(";", "").replaceAll(" ", ""));
            }
        }
        try
        {
            eventBus.register(modules.get(name).newInstance());
        } catch (InstantiationException | IllegalAccessException e)
        {
            throw new ModuleInitializationException(e);
        }
        activesAddons.add(annot.name());
    }
}

package eu.thog92.generator;

import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.api.annotations.Module;
import eu.thog92.generator.api.events.EventBus;
import eu.thog92.generator.api.events.InitEvent;
import eu.thog92.generator.core.TasksManager;
import eu.thog92.generator.core.exception.ModuleInitializationException;
import eu.thog92.generator.core.http.HttpServerManager;
import eu.thog92.generator.core.loader.AnnotationFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotGeneratorImpl extends BotGenerator
{
    private final List<String> activesAddons = new ArrayList<>();
    private AnnotationFinder loader;

    private BotGeneratorImpl() throws IllegalAccessException, IOException
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
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void initModules()
    {
        long startTime = System.currentTimeMillis();
        loader = new AnnotationFinder();
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
        System.out.println("Active modules " + activesAddons.size());


        //System.out.println((System.currentTimeMillis() - startTime) + "ms");
    }

    private void loadModule(Map<String, Class> modules, String name) throws ModuleInitializationException
    {
        if (activesAddons.contains(name) || name.equals(" ") || name.equals("")) return;

        Module annot = loader.getAnnotFromClass(name);
        if (annot == null)
            throw new ModuleInitializationException(name + " not found!");

        if (annot.dependencies() != "")
        {
            for (String dependency : annot.dependencies().split("after:"))
            {
                this.loadModule(modules, dependency.replace(";", "").replaceAll(" ", ""));
            }
        }
        try
        {
            eventBus.register(modules.get(name).newInstance());
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        activesAddons.add(annot.name());
    }
}

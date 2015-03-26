package eu.thog92.generator;

import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.api.annotations.Module;
import eu.thog92.generator.api.events.EventBus;
import eu.thog92.generator.api.events.InitEvent;
import eu.thog92.generator.core.TasksManager;
import eu.thog92.generator.core.http.HttpServerManager;
import eu.thog92.generator.core.loader.AnnotationFinder;
import eu.thog92.generator.core.tasks.GeneratorTask;
import eu.thog92.generator.core.tasks.TwitterTask;

import java.io.IOException;
import java.util.List;

public class BotGeneratorImpl extends BotGenerator
{
    private TwitterTask drama;
    private GeneratorTask generatorTask;

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
        AnnotationFinder loader = new AnnotationFinder();
        List<Class> modules = loader.search(Module.class);
        System.out.println("Found " + modules.size() + " modules");
        eventBus = new EventBus();
        for(Class clazz : modules)
        {
            try
            {
                eventBus.register(clazz.newInstance());
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }

        this.eventBus.post(new InitEvent());
        System.out.println((System.currentTimeMillis() - startTime) + "ms");
    }
}

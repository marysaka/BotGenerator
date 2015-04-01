package eu.thog92.generator.core;

import eu.thog92.generator.api.Dictionary;
import eu.thog92.generator.api.tasks.ITaskManager;
import eu.thog92.generator.api.tasks.ScheduledTask;
import twitter4j.Twitter;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TasksManager implements ITaskManager
{

    private final HashMap<ScheduledTask, ScheduledFuture<?>> activeTasks = new HashMap<>();
    private Twitter twitter;
    private Dictionary dictionary;
    private ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(4);

    public void scheduleTask(ScheduledTask task)
    {
        System.out.println("Scheduling " + task.getName() + "...");
        this.activeTasks.put(task, scheduler.scheduleAtFixedRate(task, 0,
                task.getDelay(), TimeUnit.SECONDS));
    }

    public void resetExecutorService()
    {
        scheduler.shutdownNow();
        scheduler = Executors.newScheduledThreadPool(100);
    }

    public void onFinishTask(ScheduledTask task)
    {
        if (task.isCancelled())
        {
            if (this.activeTasks.get(task) != null)
            {
                this.activeTasks.remove(task).cancel(true);
            }
        }
    }
}

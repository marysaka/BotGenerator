package eu.thog92.generator.api.tasks;

import java.util.concurrent.ScheduledExecutorService;

public interface ITaskManager
{
    void scheduleTask(ScheduledTask task);

    void onFinishTask(ScheduledTask task);

    ScheduledExecutorService getScheduler();
}

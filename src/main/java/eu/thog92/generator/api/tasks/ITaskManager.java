package eu.thog92.generator.api.tasks;

public interface ITaskManager
{
    void scheduleTask(ScheduledTask task);

    void onFinishTask(ScheduledTask task);
}

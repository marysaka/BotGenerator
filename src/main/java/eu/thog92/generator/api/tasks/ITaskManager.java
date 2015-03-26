package eu.thog92.generator.api.tasks;

import java.io.IOException;

public interface ITaskManager
{
    void scheduleTask(ScheduledTask task);

    void onFinishTask(ScheduledTask task);
}

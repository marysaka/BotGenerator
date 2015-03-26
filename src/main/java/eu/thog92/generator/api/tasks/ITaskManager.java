package eu.thog92.generator.api.tasks;

import java.io.IOException;

public interface ITaskManager
{

    void reload() throws IOException;

    void scheduleTask(ScheduledTask task);

    void onFinishTask(ScheduledTask task);
}

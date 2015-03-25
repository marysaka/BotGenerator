package eu.thog92.generator.api.tasks;

import eu.thog92.generator.core.Config;

import java.io.IOException;

public interface ITaskManager
{

    void reload() throws IOException;

    void setConfig(Config config);

    void scheduleTask(ScheduledTask task);

    void onFinishTask(ScheduledTask task);

}

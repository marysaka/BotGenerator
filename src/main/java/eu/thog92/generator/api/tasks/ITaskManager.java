package eu.thog92.generator.api.tasks;

import eu.thog92.generator.core.Config;

import java.io.IOException;

public interface ITaskManager
{

    public void reload() throws IOException;

    public void setConfig(Config config);

    public void scheduleTask(ScheduledTask task);

    public void onFinishTask(ScheduledTask task);

}

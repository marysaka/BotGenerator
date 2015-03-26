package eu.thog92.generator.api.tasks;

import eu.thog92.generator.core.Config;
import twitter4j.Twitter;

import java.io.IOException;

public interface ITaskManager
{

    void reload() throws IOException;

    void scheduleTask(ScheduledTask task);

    void onFinishTask(ScheduledTask task);
}

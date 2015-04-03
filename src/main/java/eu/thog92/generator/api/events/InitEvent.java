package eu.thog92.generator.api.events;

import eu.thog92.generator.api.BotGenerator;

import java.io.File;

public class InitEvent implements IEvent
{
    private final BotGenerator botGenerator;
    private final File configDir;

    public InitEvent()
    {
        this.botGenerator = BotGenerator.getInstance();
        this.configDir = new File("config");
    }


    public BotGenerator getBotGenerator()
    {
        return botGenerator;
    }

    public File getConfigDir()
    {
        return configDir;
    }
}

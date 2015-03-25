package eu.thog92.generator.api.events;

import eu.thog92.generator.api.BotGenerator;

public abstract class Event
{
    private final BotGenerator botGenerator;

    public Event()
    {
        this.botGenerator = BotGenerator.getInstance();
    }


    public BotGenerator getBotGenerator()
    {
        return botGenerator;
    }
}

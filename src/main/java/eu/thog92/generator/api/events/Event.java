package eu.thog92.generator.api.events;

import eu.thog92.generator.api.BotGenerator;

/**
 * Created by Thog92 on 25/03/2015.
 */
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

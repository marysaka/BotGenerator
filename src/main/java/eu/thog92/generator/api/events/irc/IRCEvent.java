package eu.thog92.generator.api.events.irc;

import eu.thog92.generator.api.events.IEvent;
import eu.thog92.generator.api.irc.IRCClient;

public class IRCEvent implements IEvent
{
    private final IRCClient ircClient;

    public IRCEvent(IRCClient instance)
    {
        this.ircClient = instance;
    }


}

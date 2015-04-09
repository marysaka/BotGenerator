package eu.thog92.generator.api.events.irc;

import eu.thog92.generator.api.events.IEvent;
import eu.thog92.generator.api.irc.IRCClient;

public class IRCReady implements IEvent
{
    private final IRCClient ircClient;

    public IRCReady(IRCClient instance)
    {
        this.ircClient = instance;
    }

    public IRCClient getIRCClient()
    {
        return ircClient;
    }
}

package eu.thog92.generator.api.events.irc;

import eu.thog92.generator.api.events.IEvent;
import eu.thog92.generator.api.irc.IRCClient;

public class IRCPrivateMessage implements IEvent
{
    private final IRCClient ircClient;
    private final String sender;
    private final String message;

    public IRCPrivateMessage(IRCClient instance, String sender, String message)
    {
        this.ircClient = instance;
        this.sender = sender;
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public String getSender()
    {
        return sender;
    }

    public IRCClient getIRCClient()
    {
        return ircClient;
    }
}

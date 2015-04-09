package eu.thog92.generator.api.events.irc;

import eu.thog92.generator.api.events.IEvent;
import eu.thog92.generator.api.irc.IRCClient;

/**
 * Created by Thog92 on 09/04/2015.
 */
public class IRCChannelMessage implements IEvent
{
    private final IRCClient ircClient;
    private final String sender;
    private final String message;
    private final String channel;

    public IRCChannelMessage(IRCClient instance, String channel, String sender, String message)
    {
        this.ircClient = instance;
        this.channel = channel;
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

    public String getChannel()
    {
        return channel;
    }

    public IRCClient getIRCClient()
    {
        return ircClient;
    }
}

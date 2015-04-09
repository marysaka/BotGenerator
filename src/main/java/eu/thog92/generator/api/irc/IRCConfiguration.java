package eu.thog92.generator.api.irc;

/**
 * Created by Thog92 on 09/04/2015.
 */
public class IRCConfiguration
{
    public boolean enabled;
    public String hostname;
    public int port;
    public String username;
    public String[] channels;


    public IRCConfiguration()
    {

    }

    public IRCConfiguration(String hostname, int port, String username, String... channels)
    {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.channels = channels;
    }
}

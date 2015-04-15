package eu.thog92.generator.api.irc;


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

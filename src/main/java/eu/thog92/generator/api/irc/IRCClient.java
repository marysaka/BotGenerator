package eu.thog92.generator.api.irc;

import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.api.events.EventBus;
import eu.thog92.generator.api.events.irc.IRCChannelMessage;
import eu.thog92.generator.api.events.irc.IRCPrivateMessage;
import eu.thog92.generator.api.events.irc.IRCReady;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IRCClient
{

    private final EventBus eventBus;
    private String host, username;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private PrintStream printStream;
    private List<String> channels;
    private int port;
    private String serverPassword;

    private IRCClient(String host, int port, String username)
    {
        this.host = host;
        this.username = username;
        this.port = port;
        this.printStream = System.out;
        this.eventBus = BotGenerator.getInstance().getEventBus();
        this.channels = new ArrayList<>();
    }


    public IRCClient connect() throws IOException
    {
        this.socket = new Socket(host, port);
        this.socket.setKeepAlive(true);
        this.socket.setSoTimeout(200000);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.login();
        this.startLoop();
        return this;
    }

    public IRCClient setPrintStream(PrintStream printStream)
    {
        this.printStream = printStream;
        return this;
    }
    private void startLoop()
    {
        final IRCClient instance = this;
        new Thread() {
            public void run()
            {
                String line;
                while ((line = getLastLine()) != null)
                {
                    printStream.println("STARTUP: " + line);
                    if (line.contains("004"))
                    {
                        // We are now logged in.
                        printStream.println("Logged in!");
                        eventBus.post(new IRCReady(instance));
                        channels.forEach(IRCClient.this::joinChannel);
                        break;
                    } else if (line.startsWith("PING"))
                    {
                        pong(line.substring(5), true);
                    } else if (line.contains("433"))
                    {
                        printStream.println("Nickname is already in use.");
                        return;
                    }
                }

                // Keep reading lines from the server.
                while ((line = getLastLine()) != null)
                {
                    if (line.startsWith("PING"))
                    {
                        // We must respond to PINGs to avoid being disconnected.
                        pong(line, false);
                    } else if (line.contains("PRIVMSG #"))
                    {
                        String sender = line.substring(1, line.indexOf("!"));
                        String channel = line.substring(line.indexOf("#"), line.indexOf(":", line.indexOf("#")));
                        String str = "PRIVMSG " + channel + ":";
                        str = line.substring(line.indexOf(str) + str.length());
                        printStream.println("[" + channel.replaceAll(" ", "") + "] <" + sender + "> " + str);
                        eventBus.post(new IRCChannelMessage(instance, channel, sender, str));
                    }
                    else if (line.contains("PRIVMSG " + username))
                    {
                        String sender = line.substring(1, line.indexOf("!"));
                        String str = "PRIVMSG " + sender + ":";
                        str = line.substring(line.indexOf(str) + str.length());
                        printStream.println("<" + sender + "> " + str);
                        eventBus.post(new IRCPrivateMessage(instance, sender, str));
                    }
                    else
                    {
                        // Print the raw line received by the bot.
                        printStream.println(line);
                    }
                }
            }
        }.start();
    }


    public void login() throws IOException
    {

        if (this.serverPassword != null)
        {
            this.writeToBuffer("PASS " + this.serverPassword + "\r\n");
        }


        this.writeToBuffer("NICK " + username + "\r\n");
        this.writeToBuffer("USER " + username + " 0 * :" + username + "\r\n");

    }


    public IRCClient joinChannel(final String channel)
    {
        writeToBuffer("JOIN " + channel + "\r\n");
        return this;
    }

    public IRCClient addChannels(String... channels)
    {
        Collections.addAll(this.channels, channels);
        return this;
    }


    public IRCClient quit(final String reason)
    {
        new Thread()
        {

            public void run()
            {
                try
                {
                    out.write("QUIT " + reason + "\r\n");
                    out.flush();
                } catch (IOException e)
                {
                    System.exit(1);
                }
            }
        }.start();

        return this;

    }


    public void sendToChat(String channel, String message)
    {
        try
        {
            out.write("PRIVMSG " + channel + " :" + message + "\r\n");
            out.flush();
        } catch (IOException e)
        {
            System.err.println("Error while send Chat Message " + message);
        }

    }


    public void writeToBuffer(final String buffer)
    {
        try
        {
            out.write(buffer);
            out.flush();
        } catch (IOException e)
        {
            System.err.println("Error while send " + buffer);
        }
    }


    public String getLastLine()
    {
        try
        {
            return in.readLine();
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public void pong(final String id, final boolean space)
    {
        printStream.println(id);
        try
        {
            if (space) out.write("PONG " + id + "\r\n");
            else out.write(id.replace("PING", "PONG"));
            out.flush();
        } catch (IOException e)
        {
            System.err.println("Error while pong " + id + " (" + e +")");
        }
    }

    public List<String> getChannelList()
    {
        return channels;
    }


    public IRCClient setServerPassword(String pass)
    {
        this.serverPassword = pass;
        return this;
    }

    public static IRCClient createIRCClient(String host, int port, String username)
    {
        return new IRCClient(host, port, username);
    }

}

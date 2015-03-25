package eu.thog92.generator.core.http;

import com.sun.net.httpserver.HttpServer;
import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.api.events.HttpInitEvent;
import eu.thog92.generator.core.http.handler.ResourceHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager
{

    public HttpServerManager(BotGenerator instance)
    {
        HttpServer server;
        try
        {
            server = HttpServer.create(new InetSocketAddress(instance.getConfig().port), 0);
            server.createContext("/", new ResourceHandler());
            instance.getEventBus().post(new HttpInitEvent(server));
            server.setExecutor(null); // creates a default executor
            server.start();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}

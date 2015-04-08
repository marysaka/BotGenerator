package eu.thog92.generator.core.http;

import com.sun.net.httpserver.HttpServer;
import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.api.IHttpServerManager;
import eu.thog92.generator.api.events.HttpStartEvent;
import eu.thog92.generator.core.http.handler.ResourceHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager implements IHttpServerManager
{

    public HttpServer createHTTPServer(int port) throws IOException
    {
        ResourceHandler resourcesHandler = new ResourceHandler();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", resourcesHandler);
        server.setExecutor(null); // creates a default executor

        BotGenerator.getInstance().getEventBus().post(new HttpStartEvent(server, resourcesHandler));
        server.start();

        return server;
    }

}

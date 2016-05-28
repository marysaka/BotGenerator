package eu.thog92.generator.core.http;

import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.api.IHttpServerManager;
import eu.thog92.generator.api.events.HttpStartEvent;
import eu.thog92.generator.api.http.HttpServer;
import eu.thog92.generator.core.http.handler.ResourceHandler;

import java.io.File;
import java.io.IOException;

public class HttpServerManager implements IHttpServerManager
{

    public HttpServer createHTTPServer(int port, File sslDir) throws IOException
    {
        ResourceHandler resourcesHandler = new ResourceHandler();
        HttpServer server = HttpServer.create(port, sslDir);
        server.setDefaultContext(resourcesHandler);
        BotGenerator.getInstance().getEventBus().post(new HttpStartEvent(server, resourcesHandler));
        server.start();

        return server;
    }

}

package eu.thog92.generator.api.events;

import com.sun.net.httpserver.HttpServer;

public class HttpStartEvent extends Event
{
    private final HttpServer httpServer;

    public HttpStartEvent(HttpServer server)
    {
        this.httpServer = server;
    }

    public HttpServer getServer()
    {
        return httpServer;
    }
}
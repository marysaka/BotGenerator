package eu.thog92.generator.api.events;

import com.sun.net.httpserver.HttpServer;
import eu.thog92.generator.api.IResourceHandler;

public class HttpStartEvent implements IEvent
{
    private final HttpServer httpServer;
    private final IResourceHandler resourcesHandler;

    public HttpStartEvent(HttpServer server, IResourceHandler resourcesHandler)
    {
        this.httpServer = server;
        this.resourcesHandler = resourcesHandler;
    }

    public HttpServer getServer()
    {
        return httpServer;
    }

    public IResourceHandler getResourcesHandler()
    {
        return resourcesHandler;
    }
}

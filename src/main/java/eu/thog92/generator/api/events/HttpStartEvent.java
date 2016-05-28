package eu.thog92.generator.api.events;
import eu.thog92.generator.api.http.IResourceHandler;
import eu.thog92.generator.api.http.HttpServer;

public class HttpStartEvent implements IEvent
{
    private final HttpServer httpServer;
    private final IResourceHandler resourcesHandler;

    public HttpStartEvent(HttpServer server, IResourceHandler resourcesHandler)
    {
        this.httpServer = server;
        this.resourcesHandler = resourcesHandler;
    }

    @Deprecated
    public HttpServer getServer()
    {
        return httpServer;
    }

    public IResourceHandler getResourcesHandler()
    {
        return resourcesHandler;
    }
}

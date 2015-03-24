package eu.thog92.generator.core.http;

import eu.thog92.generator.api.BotGenerator;

public class HttpServerManager
{

    public HttpServerManager(BotGenerator instance)
    {
        /*HttpServer server;
        try
        {
            server = HttpServer.create(new InetSocketAddress(cfg.port), 0);
            server.createContext("/", new ResourceHandler());
            //server.createContext("/refresh", new RefreshHandler(instance));
            server.createContext("/drama", new DramaHandler(instance.getGeneratorTask(), false));
            server.createContext("/api/drama", new DramaHandler(instance.getGeneratorTask(), true));
            server.setExecutor(null); // creates a default executor
            server.start();

        } catch (IOException e)
        {
            e.printStackTrace();
        }*/

    }

}

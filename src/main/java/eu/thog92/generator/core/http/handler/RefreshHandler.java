package eu.thog92.generator.core.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.thog92.generator.BotGeneratorImpl;

import java.io.IOException;
import java.io.OutputStream;


@Deprecated
public class RefreshHandler implements HttpHandler
{

    private BotGeneratorImpl main;

    public RefreshHandler(BotGeneratorImpl instance)
    {
        this.main = instance;
    }

    @Override
    public void handle(HttpExchange ext) throws IOException
    {
        main.reload();
        String response = "Config Reloaded";
        ext.sendResponseHeaders(200, response.length());
        OutputStream os = ext.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}

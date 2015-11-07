package eu.thog92.generator.core.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import eu.thog92.generator.api.IResourceHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

public class ResourceHandler implements HttpHandler, IResourceHandler
{

    private String defaultPage = "/public/index.html";
    private String default404Message = "File not found";

    @Override
    public void handle(HttpExchange ext) throws IOException
    {
        OutputStream os = ext.getResponseBody();
        InputStream in = null;
        try
        {
            String path = ext.getRequestURI().toString().replaceFirst("/", "/public/");
            if (path.equalsIgnoreCase("/public/"))
            {
                path = defaultPage;
            }
            URL resource = ResourceHandler.class.getResource(path);
            in = ResourceHandler.class.getResourceAsStream(path);
            if (resource == null)
            {

                ext.sendResponseHeaders(404, default404Message.length());
                os.write(default404Message.getBytes(Charset.forName("UTF-8")));
                os.close();
                return;
            }
            int len;
            byte[] buf = new byte[1024];
            ext.sendResponseHeaders(200, new File(resource.getFile()).length());
            while ((len = in.read(buf, 0, 1024)) != -1)
                os.write(buf, 0, len);
            in.close();
            os.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                if (in != null)
                    in.close();
            } catch (IOException ignored) {}
            os.write(default404Message.getBytes(Charset.forName("UTF-8")));
            os.close();
        }

    }

    public void setDefaultPage(String path)
    {
        this.defaultPage = path;
    }

    public void set404Message(String message)
    {
        this.default404Message = message;
    }
}

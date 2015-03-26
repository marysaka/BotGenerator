package eu.thog92.generator.api;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public interface IHttpServerManager
{
    HttpServer createHTTPServer(int port) throws IOException;
}

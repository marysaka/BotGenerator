package eu.thog92.generator.api;

import eu.thog92.generator.api.http.HttpServer;

import java.io.File;
import java.io.IOException;

public interface IHttpServerManager
{
    HttpServer createHTTPServer(int port, File sslDir) throws IOException;
}

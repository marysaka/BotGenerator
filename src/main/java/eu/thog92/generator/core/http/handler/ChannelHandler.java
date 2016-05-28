package eu.thog92.generator.core.http.handler;

import eu.thog92.generator.api.http.HttpServer;
import eu.thog92.generator.api.http.IRequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import static eu.thog92.generator.api.http.HttpServer.sanitizeUri;
import static eu.thog92.generator.api.http.HttpServer.sendError;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;

/**
 * Desc...
 * Created by Thog the 28/05/2016
 */
public class ChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest>
{
    private final HttpServer server;

    public ChannelHandler(HttpServer server)
    {
        this.server = server;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        if (ctx.channel().isActive())
        {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception
    {
        final String uri = request.uri();
        final String path = sanitizeUri(uri);
        if (path == null)
        {
            sendError(ctx, FORBIDDEN);
            return;
        }

        IRequestHandler handler = server.getHandler(path);
        if (handler == null)
            handler = server.getDefaultHandler();
        handler.handle(ctx, request, path);
    }
}

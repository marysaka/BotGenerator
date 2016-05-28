package eu.thog92.generator.api.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Desc...
 * Created by Thog the 28/05/2016
 */
public interface IRequestHandler
{
    void handle(ChannelHandlerContext ctx, FullHttpRequest request, String path) throws Exception;
}

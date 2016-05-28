package eu.thog92.generator.core.http.handler;


import eu.thog92.generator.api.http.IRequestHandler;
import eu.thog92.generator.api.http.IResourceHandler;
import eu.thog92.generator.util.MineTypeDatabase;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static eu.thog92.generator.api.http.HttpServer.*;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ResourceHandler implements IResourceHandler, IRequestHandler
{
    private final Map<String, File> RESOURCE_CACHE = new HashMap<>();
    private String defaultPage = "/public/index.html";

    public File getResourceAsFile(String resourcePath)
    {
        if (RESOURCE_CACHE.containsKey(resourcePath) && RESOURCE_CACHE.get(resourcePath).exists())
            return RESOURCE_CACHE.get(resourcePath);

        try
        {
            InputStream in = ResourceHandler.class.getResourceAsStream(resourcePath);
            if (in == null)
            {
                return null;
            }

            File tempFile = File.createTempFile(String.valueOf(in.hashCode()), "." + MineTypeDatabase.getExtension(resourcePath));
            RESOURCE_CACHE.put(resourcePath, tempFile);
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile))
            {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void handle(ChannelHandlerContext ctx, FullHttpRequest request, String path) throws Exception
    {
        if (!request.decoderResult().isSuccess())
        {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        if (request.method() != GET)
        {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }

        path = path.replaceFirst("/", "/public/");
        if (path.equalsIgnoreCase("/public/"))
            path = defaultPage;
        File file = getResourceAsFile(path);

        if (file == null || !file.exists())
        {
            sendError(ctx, NOT_FOUND);
            return;
        }

        // Cache Validation
        String ifModifiedSince = request.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE);
        if (ifModifiedSince != null && !ifModifiedSince.isEmpty())
        {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
            Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);

            // Only compare up to the second because the datetime format we send to the client
            // does not have milliseconds
            long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
            long fileLastModifiedSeconds = file.lastModified() / 1000;
            if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds)
            {
                sendNotModified(ctx);
                return;
            }
        }

        RandomAccessFile raf;
        try
        {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException ignore)
        {
            ignore.printStackTrace();
            sendError(ctx, NOT_FOUND);
            return;
        }

        long fileLength = raf.length();

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        HttpUtil.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);
        setDateAndCacheHeaders(response, file);
        if (HttpUtil.isKeepAlive(request))
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        // Write the initial line and the header.
        ctx.write(response);

        // Write the content.
        ChannelFuture sendFileFuture;
        ChannelFuture lastContentFuture;
        if (ctx.pipeline().get(SslHandler.class) == null)
        {
            sendFileFuture =
                    ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
            // Write the end marker.
            lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } else
        {
            sendFileFuture =
                    ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
                            ctx.newProgressivePromise());
            // HttpChunkedInput will write the end marker (LastHttpContent) for us.
            lastContentFuture = sendFileFuture;
        }

        sendFileFuture.addListener(new ChannelProgressiveFutureListener()
        {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
            {
                // NOP
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future)
            {
                // NOP
            }
        });

        // Decide whether to close the connection or not.
        if (!HttpUtil.isKeepAlive(request))
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void setDefaultPage(String path)
    {
        this.defaultPage = path;
    }
}
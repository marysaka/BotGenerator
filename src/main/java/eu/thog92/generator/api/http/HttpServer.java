package eu.thog92.generator.api.http;

import eu.thog92.generator.api.BotGenerator;
import eu.thog92.generator.core.http.handler.ChannelHandler;
import eu.thog92.generator.util.MineTypeDatabase;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_MODIFIED;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Abstract layout for the http server
 * Created by Thog the 28/05/2016
 */
public class HttpServer
{

    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    private static final int HTTP_CACHE_SECONDS = 60;
    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
    private final int port;
    private IRequestHandler defaultContext;
    private final Map<String, IRequestHandler> HANDLERS = new HashMap<>();
    private final ServerBootstrap bootstrap;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private HttpServer(int port, File sslDir)
    {
        this.port = port;
        HttpServer server = this;
        // Configure SSL.
        SslContext sslCtx = null;

        try
        {
            File sslCert = new File(sslDir, "server.cert");
            File sslPrivKey = new File(sslDir, "server.key");
            if (sslCert.exists() && sslPrivKey.exists())
                sslCtx = SslContextBuilder.forServer(sslCert, sslPrivKey).sslProvider(SslProvider.OPENSSL).build();
        } catch (SSLException e)
        {
            e.printStackTrace();
        }


        bootstrap = new ServerBootstrap();
        SslContext finalSslCtx = sslCtx;
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception
                    {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (finalSslCtx != null)
                        {
                            pipeline.addLast(finalSslCtx.newHandler(ch.alloc()));
                        }
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(65536));
                        pipeline.addLast(new ChunkedWriteHandler());
                        pipeline.addLast(new ChannelHandler(server));
                    }
                });
    }

    public static HttpServer create(int port, File sslDir)
    {
        return new HttpServer(port, sslDir);
    }

    public void createContext(String entryPoint, IRequestHandler resourcesHandler)
    {
        HANDLERS.put(entryPoint, resourcesHandler);
    }

    public void start()
    {
        BotGenerator.getInstance().getTasksManager().getScheduler().execute(() -> {
            try
            {
                Channel ch = bootstrap.bind(port).sync().channel();
                ch.closeFuture().sync();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            } finally
            {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
    }

    public void setDefaultContext(IRequestHandler defaultContext)
    {
        this.defaultContext = defaultContext;
    }

    public static String sanitizeUri(String uri)
    {
        // Decode the path.
        try
        {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            throw new Error(e);
        }

        if (uri.isEmpty() || uri.charAt(0) != '/')
        {
            return null;
        }

        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (uri.contains(File.separator + '.') ||
                uri.contains('.' + File.separator) ||
                uri.charAt(0) == '.' || uri.charAt(uri.length() - 1) == '.' ||
                INSECURE_URI.matcher(uri).matches())
        {
            return null;
        }

        // Convert to absolute path.
        return uri;
    }

    public static void sendRedirect(ChannelHandlerContext ctx, String newUri)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * When file timestamp is the same as what the browser is sending up, send a "304 Not Modified"
     *
     * @param ctx Context
     */
    public static void sendNotModified(ChannelHandlerContext ctx)
    {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_MODIFIED);
        setDateHeader(response);

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Sets the Date header for the HTTP response
     *
     * @param response HTTP response
     */
    public static void setDateHeader(FullHttpResponse response)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        Calendar time = new GregorianCalendar();
        response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));
    }

    /**
     * Sets the Date and Cache headers for the HTTP Response
     *
     * @param response    HTTP response
     * @param fileToCache file to extract content type
     */
    public static void setDateAndCacheHeaders(HttpResponse response, File fileToCache)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        // Date header
        Calendar time = new GregorianCalendar();
        response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));

        // Add cache headers
        time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
        response.headers().set(HttpHeaderNames.EXPIRES, dateFormatter.format(time.getTime()));
        response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
        response.headers().set(
                HttpHeaderNames.LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
    }

    /**
     * Sets the content type header for the HTTP Response
     *
     * @param response HTTP response
     * @param f        file  to extract content type
     */
    public static void setContentTypeHeader(HttpResponse response, File f)
    {
        String mineType = null;
        try
        {
            mineType = Files.probeContentType(f.toPath());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (mineType == null) mineType = MineTypeDatabase.getMineType(f);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mineType);
    }

    public IRequestHandler getHandler(String entryPoint)
    {
        IRequestHandler possibleResult = null;
        for (String point : HANDLERS.keySet())
        {
            if (entryPoint.equals(point))
                return HANDLERS.get(point);
            else if (entryPoint.startsWith(point))
                possibleResult = HANDLERS.get(point);
        }
        return possibleResult;
    }

    public IRequestHandler getDefaultHandler()
    {
        return defaultContext;
    }
}

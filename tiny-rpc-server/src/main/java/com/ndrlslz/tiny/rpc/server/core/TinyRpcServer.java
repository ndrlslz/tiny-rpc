package com.ndrlslz.tiny.rpc.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

public class TinyRpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcServer.class);
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Object serviceImpl;
    private TinyRpcServerOptions tinyRpcServerOptions;
    private EventExecutorGroup requestHandlerGroup;

    private TinyRpcServer() {

    }

    public static TinyRpcServer create() {
        return new TinyRpcServer();
    }

    public static TinyRpcServer create(TinyRpcServerOptions options) {
        TinyRpcServer tinyRpcServer = create();
        tinyRpcServer.tinyRpcServerOptions = options;
        return tinyRpcServer;
    }

    public TinyRpcServer registerService(Object serviceImpl) {
        this.serviceImpl = serviceImpl;

        return this;
    }

    public TinyRpcServer listen(int port) {
        if (nonNull(tinyRpcServerOptions) && nonNull(tinyRpcServerOptions.getWorkerThreadCount())) {
            requestHandlerGroup = new DefaultEventExecutorGroup(tinyRpcServerOptions.getWorkerThreadCount());
        }

        RequestHandler tinyRpcRequestHandler = new TinyRpcRequestHandler(serviceImpl);
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new TinyRpcServerInitializer(requestHandlerGroup, tinyRpcRequestHandler));

            bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return this;
    }

    public void close() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}

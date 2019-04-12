package com.ndrlslz.tiny.rpc.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.EventExecutorGroup;

public class TinyRpcServer {
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Object serviceImpl;
    //        private TinyRpcServerOptions TinyRpcServerOptions;
    private static EventExecutorGroup requestHandlerGroup;

    private TinyRpcServer() {

    }

    public static TinyRpcServer create() {
        return new TinyRpcServer();
    }

    public TinyRpcServer registerService(Object serviceImpl) {
        this.serviceImpl = serviceImpl;

        return this;
    }

    public TinyRpcServer listen(int port) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new TinyRpcServerInitializer(requestHandlerGroup));

            bootstrap.bind(port).sync();

        } catch (InterruptedException e) {
        }
        return this;
    }

    public void close() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}

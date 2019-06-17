package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.UUID;

public class TinyRpcClient {
    private String host;
    private int port;
    private NioEventLoopGroup workerGroup;
    private final Bootstrap bootstrap;

    public TinyRpcClient(String host, int port) {
        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new TinyRpcClientInitializer());
                    }
                });

        this.host = host;
        this.port = port;
    }

    public Object send(String method, Object[] parameters) throws Exception {
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();
        tinyRpcRequest.setMethodName(method);
        tinyRpcRequest.setArgumentsValue(parameters);
        tinyRpcRequest.setCorrelationId(UUID.randomUUID().toString());

        channelFuture.channel().writeAndFlush(tinyRpcRequest);
        ChannelHandler channelHandler = channelFuture.channel().pipeline().last();

        channelFuture.channel().closeFuture().sync();

        TinyRpcClientHandler handler = (TinyRpcClientHandler) channelHandler;
        return handler.result;
    }

    public void close() {
        workerGroup.shutdownGracefully();
    }
}
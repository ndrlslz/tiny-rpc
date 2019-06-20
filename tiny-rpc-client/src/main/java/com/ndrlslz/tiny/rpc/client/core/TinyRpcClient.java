package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.client.exception.TinyRpcTimeoutException;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcServiceOptions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public class TinyRpcClient {
    private String host;
    private int port;
    private NioEventLoopGroup workerGroup;
    private final Bootstrap bootstrap;
    private TinyRpcServiceOptions tinyRpcServiceOptions;

    public TinyRpcClient(String host, int port, TinyRpcServiceOptions tinyRpcServiceOptions) {
        this.tinyRpcServiceOptions = isNull(tinyRpcServiceOptions) ? new TinyRpcServiceOptions() : tinyRpcServiceOptions;

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

    public Object invoke(String method, Object[] parameters) throws Exception {
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

        TinyRpcRequest tinyRpcRequest = buildTinyRpcRequest(method, parameters);

        channelFuture.channel().writeAndFlush(tinyRpcRequest);

        TinyRpcClientHandler handler = (TinyRpcClientHandler) channelFuture.channel().pipeline().last();
        Object result = handler.resultQueue.poll(tinyRpcServiceOptions.getTimeout(), TimeUnit.SECONDS);

        channelFuture.channel().close();

        if (isNull(result)) {
            return new TinyRpcTimeoutException(format("Timeout exception, cannot get response within %s seconds", tinyRpcServiceOptions.getTimeout()));
        }

        return result;
    }

    private TinyRpcRequest buildTinyRpcRequest(String method, Object[] parameters) {
        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();
        tinyRpcRequest.setMethodName(method);
        tinyRpcRequest.setArgumentsValue(parameters);
        tinyRpcRequest.setCorrelationId(UUID.randomUUID().toString());
        return tinyRpcRequest;
    }

    public void close() {
        workerGroup.shutdownGracefully();
    }
}
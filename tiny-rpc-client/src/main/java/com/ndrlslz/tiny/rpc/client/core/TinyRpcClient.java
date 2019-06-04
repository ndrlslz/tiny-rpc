package com.ndrlslz.tiny.rpc.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

public class TinyRpcClient {
    private Channel channel;
    private NioEventLoopGroup workerGroup;

    public TinyRpcClient(String host, int port) {
        try {
            initClient(host, port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initClient(String host, int port) throws InterruptedException {
        workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new TinyRpcClientInitializer());
                    }
                });

        channel = bootstrap.connect(host, port).sync().channel();

        channel.closeFuture().sync();
    }

    public ChannelFuture send(String message) {
        return channel.writeAndFlush(message);
    }

    public void close() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);

        channel.close().addListener((ChannelFutureListener) future -> {
            System.out.println("close client");
            workerGroup.shutdownGracefully();
        });
    }
}

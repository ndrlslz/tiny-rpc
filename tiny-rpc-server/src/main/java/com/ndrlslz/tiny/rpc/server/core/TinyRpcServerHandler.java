package com.ndrlslz.tiny.rpc.server.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static io.netty.channel.ChannelFutureListener.CLOSE;

public class TinyRpcServerHandler extends SimpleChannelInboundHandler<String> {
    TinyRpcServerHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) {
        if (request.contains("exit")) {
            ctx.writeAndFlush("close").addListener(CLOSE);
        }
        System.out.println("receive " + request);
        String response = "hello " + request;
        ctx.writeAndFlush(response);

        System.out.println("send back " + response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();

        System.out.println("error");
        ctx.writeAndFlush("error").addListener(CLOSE);
    }
}
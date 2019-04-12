package com.ndrlslz.tiny.rpc.server.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static io.netty.channel.ChannelFutureListener.CLOSE;

public class TinyRpcServerHandler extends SimpleChannelInboundHandler<String> {
    TinyRpcServerHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) {
        if (request.contains("exit")) {
            ctx.writeAndFlush("close").addListener(CLOSE);
        }

        String response = "hello " + request;
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.writeAndFlush("error").addListener(CLOSE);
    }
}

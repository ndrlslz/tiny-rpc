package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyRpcClientHandler extends SimpleChannelInboundHandler<TinyRpcResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcClientHandler.class);
    public Object result;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcResponse msg) {
        LOGGER.debug("TinyRpcClient receive message, methodName: {}, type: {}, value: {}", msg.getMethodName(),
                msg.getResponseType(), msg.getResponseValue());

        result = msg.getResponseValue();
        ctx.close();
    }
}
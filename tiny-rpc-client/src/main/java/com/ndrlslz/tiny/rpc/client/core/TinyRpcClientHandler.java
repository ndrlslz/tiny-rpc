package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TinyRpcClientHandler extends SimpleChannelInboundHandler<TinyRpcResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcResponse msg) {
        LOGGER.debug("TinyRpcClient receive message: " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();
        tinyRpcRequest.setMethodName("hello");
        tinyRpcRequest.setCorrelationId("id-123");

        ctx.writeAndFlush(tinyRpcRequest);
    }
}

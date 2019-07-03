package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.client.model.NullObject;
import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

public class TinyRpcClientHandler extends SimpleChannelInboundHandler<TinyRpcResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcClientHandler.class);
    public LinkedBlockingQueue<Object> resultQueue;

    public TinyRpcClientHandler() {
        this.resultQueue = new LinkedBlockingQueue<>(1);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcResponse msg) {
        LOGGER.debug("TinyRpcClient receive message, methodName: {}, type: {}, value: {}", msg.getMethodName(),
                msg.getResponseType(), msg.getResponseValue());

        Object result = msg.getResponseValue();

        if (Objects.isNull(result)) {
            resultQueue.offer(new NullObject());
        } else {
            resultQueue.offer(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error(cause.getMessage(), cause);

        resultQueue.offer(new TinyRpcException(cause.getMessage(), cause));
    }
}
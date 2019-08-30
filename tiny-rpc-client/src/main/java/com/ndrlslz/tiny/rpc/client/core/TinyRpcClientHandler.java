package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.client.callback.MessageCallback;
import com.ndrlslz.tiny.rpc.client.callback.MessageCallbackStorage;
import com.ndrlslz.tiny.rpc.client.model.NullObject;
import com.ndrlslz.tiny.rpc.client.pool.ConnectionPool;
import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcHeartbeat;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

import static com.ndrlslz.tiny.rpc.client.pool.PooledConnection.pooledConnectionOf;
import static java.util.Objects.nonNull;

public class TinyRpcClientHandler extends SimpleChannelInboundHandler<TinyRpcResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcClientHandler.class);
    private String correlationId;
    private ConnectionPool connectionPool;

    TinyRpcClientHandler(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcResponse msg) {
        LOGGER.debug("TinyRpcClient receive message, methodName: {}, type: {}, value: {}", msg.getMethodName(),
                msg.getResponseType(), msg.getResponseValue());

        correlationId = msg.getCorrelationId();
        Object result = msg.getResponseValue();

        MessageCallback messageCallback = MessageCallbackStorage.get(correlationId);

        if (Objects.isNull(result)) {
            messageCallback.done(new NullObject());
        } else {
            messageCallback.done(result);
        }

        MessageCallbackStorage.remove(correlationId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connectionPool.removeConnection(pooledConnectionOf(ctx.channel()));
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error(cause.getMessage(), cause);

        MessageCallback messageCallback = MessageCallbackStorage.get(correlationId);

        if (nonNull(messageCallback)) {
            messageCallback.done(new TinyRpcException(cause.getMessage(), cause));
            MessageCallbackStorage.remove(correlationId);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                TinyRpcHeartbeat tinyRpcHeartbeat = new TinyRpcHeartbeat();
                tinyRpcHeartbeat.setCorrelationId(UUID.randomUUID().toString());

                LOGGER.debug("send heartbeat message");
                ctx.writeAndFlush(tinyRpcHeartbeat);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
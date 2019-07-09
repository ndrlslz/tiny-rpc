package com.ndrlslz.tiny.rpc.server.core;

import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.channel.ChannelFutureListener.CLOSE;

public class TinyRpcServerHandler extends SimpleChannelInboundHandler<TinyRpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcServerHandler.class);
    private String correlationId;
    private RequestHandler tinyRpcRequestHandler;

    TinyRpcServerHandler(RequestHandler tinyRpcRequestHandler) {
        this.tinyRpcRequestHandler = tinyRpcRequestHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcRequest msg) {
        LOGGER.debug("TinyRpcServer receive message, methodName: {}, parameter: {}", msg.getMethodName(),
                msg.getArgumentsValue());

        correlationId = msg.getCorrelationId();

        tinyRpcRequestHandler.handle(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error(cause.getMessage(), cause);

        TinyRpcResponse tinyRpcResponse = new TinyRpcResponse();

        Throwable causeReason = cause.getCause();
        tinyRpcResponse.setResponseValue(new TinyRpcException(causeReason.getMessage(), causeReason));
        tinyRpcResponse.setCorrelationId(correlationId);

        ctx.writeAndFlush(tinyRpcResponse).addListener(CLOSE);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.warn("close inactive session");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
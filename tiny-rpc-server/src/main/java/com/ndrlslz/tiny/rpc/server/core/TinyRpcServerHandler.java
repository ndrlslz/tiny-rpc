package com.ndrlslz.tiny.rpc.server.core;

import com.ndrlslz.tiny.rpc.server.exception.TinyRpcServerException;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import static io.netty.channel.ChannelFutureListener.CLOSE;

public class TinyRpcServerHandler extends SimpleChannelInboundHandler<TinyRpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcServerHandler.class);
    private String correlationId;
    private Object serviceImpl;

    TinyRpcServerHandler(Object serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcRequest msg) {
        correlationId = msg.getCorrelationId();

        Object response = invokeMethod(msg);

        TinyRpcResponse tinyRpcResponse = new TinyRpcResponse();
        tinyRpcResponse.setResponseValue(response);
        tinyRpcResponse.setCorrelationId(msg.getCorrelationId());

        ctx.writeAndFlush(tinyRpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error(cause.getMessage(), cause);

        TinyRpcResponse tinyRpcResponse = new TinyRpcResponse();

        Throwable causeReason = cause.getCause();
        tinyRpcResponse.setResponseValue(new TinyRpcServerException(causeReason.getMessage(), causeReason));
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

    private Object invokeMethod(TinyRpcRequest msg) {
        Object response;
        try {
            response = MethodUtils.invokeMethod(serviceImpl, msg.getMethodName(), msg.getArgumentsValue());
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            LOGGER.error(exception.getMessage(), exception);

            response = new TinyRpcServerException(exception.getMessage(), exception);
        } catch (InvocationTargetException exception) {
            LOGGER.error(exception.getMessage(), exception);

            Throwable targetException = exception.getTargetException();
            response = new TinyRpcServerException(targetException.getMessage(), targetException);
        }

        return response;
    }
}
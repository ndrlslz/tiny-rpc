package com.ndrlslz.tiny.rpc.server.core;

import com.ndrlslz.tiny.rpc.server.HelloServiceImpl;
import com.ndrlslz.tiny.rpc.server.exception.TinyRpcServerException;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcRequest msg) {
        System.out.println(msg.getCorrelationId());
        System.out.println(msg.getMethodName());
        System.out.println(msg.getArgumentsValue()[0]);

        HelloServiceImpl helloService = new HelloServiceImpl();

        Object response;
        try {
            response = MethodUtils.invokeMethod(helloService, msg.getMethodName(), msg.getArgumentsValue());
        } catch (NoSuchMethodException | IllegalAccessException exception) {
            response = new TinyRpcServerException(exception.getMessage(), exception);
        } catch (InvocationTargetException exception) {
            Throwable targetException = exception.getTargetException();
            response = new TinyRpcServerException(targetException.getMessage(), targetException);
        }

        TinyRpcResponse tinyRpcResponse = new TinyRpcResponse();
        tinyRpcResponse.setResponseValue(response);
        tinyRpcResponse.setCorrelationId(msg.getCorrelationId());

        ctx.writeAndFlush(tinyRpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error(cause.getMessage(), cause);

        TinyRpcResponse tinyRpcResponse = new TinyRpcResponse();
        tinyRpcResponse.setResponseValue(new TinyRpcServerException(cause.getMessage(), cause));

        ctx.writeAndFlush(tinyRpcResponse).addListener(CLOSE);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                LOGGER.warn("close unactive session");
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}

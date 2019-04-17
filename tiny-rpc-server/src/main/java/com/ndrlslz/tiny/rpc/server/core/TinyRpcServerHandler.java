package com.ndrlslz.tiny.rpc.server.core;

import com.ndrlslz.tiny.rpc.server.HelloServiceImpl;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static io.netty.channel.ChannelFutureListener.CLOSE;

public class TinyRpcServerHandler extends SimpleChannelInboundHandler<TinyRpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TinyRpcRequest msg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println(msg.getMethodName());
        System.out.println(msg.getArgumentsCount());
        System.out.println(msg.getArgumentsValue()[0]);

        HelloServiceImpl helloService = new HelloServiceImpl();

        Method sayMethod = helloService.getClass().getDeclaredMethod(msg.getMethodName(), String.class);

        Object response = sayMethod.invoke(helloService, msg.getArgumentsValue()[0]);

        TinyRpcResponse tinyRpcResponse = new TinyRpcResponse();
        tinyRpcResponse.setResponseValue(response);

        ctx.writeAndFlush(tinyRpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();

        System.out.println("error");
        ctx.writeAndFlush("error").addListener(CLOSE);
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

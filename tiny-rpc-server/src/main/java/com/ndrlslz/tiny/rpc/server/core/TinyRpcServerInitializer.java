package com.ndrlslz.tiny.rpc.server.core;

import com.ndrlslz.tiny.rpc.core.codec.ProtocolDecoder;
import com.ndrlslz.tiny.rpc.core.codec.ProtocolEncoder;
import com.ndrlslz.tiny.rpc.server.codec.TinyRpcRequestDecoder;
import com.ndrlslz.tiny.rpc.server.codec.TinyRpcResponseEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import static java.util.Objects.nonNull;

public class TinyRpcServerInitializer extends ChannelInitializer {
    private static final int HEARTBEAT_TIMEOUT_SECONDS = 10;
    private EventExecutorGroup requestHandlerGroup;
    private Object serviceImpl;

    TinyRpcServerInitializer(EventExecutorGroup requestHandlerGroup, Object serviceImpl) {
        this.requestHandlerGroup = requestHandlerGroup;
        this.serviceImpl = serviceImpl;
    }

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                .addLast(new IdleStateHandler(HEARTBEAT_TIMEOUT_SECONDS, 0, 0))
                .addLast(new ProtocolDecoder())
                .addLast(new ProtocolEncoder())
                .addLast(new TinyRpcRequestDecoder())
                .addLast(new TinyRpcResponseEncoder());

        if (nonNull(requestHandlerGroup)) {
            ch.pipeline().addLast(requestHandlerGroup, new TinyRpcServerHandler(serviceImpl));
        } else {
            ch.pipeline().addLast(new TinyRpcServerHandler(serviceImpl));
        }
    }
}

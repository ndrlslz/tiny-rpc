package com.ndrlslz.tiny.rpc.server.core;

import com.ndrlslz.tiny.rpc.server.codec.RequestPacketCodec;
import com.ndrlslz.tiny.rpc.server.codec.ResponsePacketCodec;
import com.ndrlslz.tiny.rpc.server.codec.TinyRpcRequestCodec;
import com.ndrlslz.tiny.rpc.server.codec.TinyRpcResponseCodec;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import static java.util.Objects.nonNull;

public class TinyRpcServerInitializer extends ChannelInitializer {
    private static final int HEARTBEAT_TIMEOUT_SECONDS = 10;
    private EventExecutorGroup requestHandlerGroup;

    TinyRpcServerInitializer(EventExecutorGroup requestHandlerGroup) {
        this.requestHandlerGroup = requestHandlerGroup;
    }

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                .addLast(new IdleStateHandler(HEARTBEAT_TIMEOUT_SECONDS, 0, 0))
                .addLast(new RequestPacketCodec())
                .addLast(new ResponsePacketCodec())
                .addLast(new TinyRpcRequestCodec())
                .addLast(new TinyRpcResponseCodec());

        if (nonNull(requestHandlerGroup)) {
            ch.pipeline().addLast(requestHandlerGroup, new TinyRpcServerHandler());
        } else {
            ch.pipeline().addLast(new TinyRpcServerHandler());
        }
    }
}

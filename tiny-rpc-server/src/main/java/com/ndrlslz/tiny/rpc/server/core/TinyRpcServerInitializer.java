package com.ndrlslz.tiny.rpc.server.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import static java.util.Objects.nonNull;

public class TinyRpcServerInitializer extends ChannelInitializer {
    private EventExecutorGroup requestHandlerGroup;

    TinyRpcServerInitializer(EventExecutorGroup requestHandlerGroup) {
        this.requestHandlerGroup = requestHandlerGroup;
    }

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                .addLast(new ReadTimeoutHandler(30))
                .addLast(new StringDecoder())
                .addLast(new StringEncoder());

        if (nonNull(requestHandlerGroup)) {
            ch.pipeline().addLast(requestHandlerGroup, new TinyRpcServerHandler());
        } else {
            ch.pipeline().addLast(new TinyRpcServerHandler());
        }
    }
}

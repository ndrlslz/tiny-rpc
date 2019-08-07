package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.client.codec.TinyRpcRequestEncoder;
import com.ndrlslz.tiny.rpc.client.codec.TinyRpcResponseDecoder;
import com.ndrlslz.tiny.rpc.client.pool.ConnectionPool;
import com.ndrlslz.tiny.rpc.core.codec.ProtocolDecoder;
import com.ndrlslz.tiny.rpc.core.codec.ProtocolEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.util.concurrent.EventExecutorGroup;

import static java.util.Objects.nonNull;

public class TinyRpcClientInitializer extends ChannelInitializer {
    private ConnectionPool connectionPool;

    public TinyRpcClientInitializer(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    protected void initChannel(Channel ch) {
        ch.pipeline()
                .addLast(new ProtocolDecoder())
                .addLast(new ProtocolEncoder())
                .addLast(new TinyRpcRequestEncoder())
                .addLast(new TinyRpcResponseDecoder())
                .addLast(new TinyRpcClientHandler(connectionPool));
    }
}
package com.ndrlslz.tiny.rpc.client.pool;

import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import io.netty.channel.Channel;

import java.util.Objects;


public class PooledConnection {
    private Channel channel;

    public PooledConnection(Channel channel) {
        this.channel = channel;
    }

    public void writeAndFlush(TinyRpcRequest tinyRpcRequest) {
        this.channel.writeAndFlush(tinyRpcRequest);
    }

    public boolean isActive() {
        return channel.isActive();
    }

    public void close() {
        channel.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PooledConnection that = (PooledConnection) o;
        return Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel);
    }
}

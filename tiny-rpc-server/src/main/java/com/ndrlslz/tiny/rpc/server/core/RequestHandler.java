package com.ndrlslz.tiny.rpc.server.core;

import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import io.netty.channel.ChannelHandlerContext;

@FunctionalInterface
public interface RequestHandler {
    void handle(ChannelHandlerContext ctx, TinyRpcRequest msg);
}

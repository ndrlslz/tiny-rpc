package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.protocol.RpcMessage;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class TinyRpcResponseCodec extends MessageToMessageCodec<RpcMessage<TinyRpcResponse>, TinyRpcResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TinyRpcResponse msg, List<Object> out) {
        RpcMessage<TinyRpcResponse> result = new RpcMessage<>();
        result.setBody(msg);

        out.add(result);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, RpcMessage<TinyRpcResponse> msg, List<Object> out) {
        out.add(msg.getBody());
    }
}

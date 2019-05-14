package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.protocol.RpcMessage;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

public class TinyRpcRequestCodec extends MessageToMessageCodec<RpcMessage<TinyRpcRequest>, TinyRpcRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TinyRpcRequest msg, List<Object> out) {
        RpcMessage<TinyRpcRequest> result = new RpcMessage<>();
        result.setBody(msg);

        out.add(result);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, RpcMessage<TinyRpcRequest> msg, List<Object> out) {
        out.add(msg.getBody());
    }
}

package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.protocol.RpcMessage;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class TinyRpcResponseCodec extends ByteToMessageCodec<RpcMessage<TinyRpcResponse>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage<TinyRpcResponse> msg, ByteBuf out) {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

    }
}

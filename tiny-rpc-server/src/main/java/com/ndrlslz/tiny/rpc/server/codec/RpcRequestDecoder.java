package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.server.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.server.serialization.HessianSerializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class RpcRequestDecoder extends MessageToMessageDecoder<ProtocolBody> {
    private static final HessianSerializer serializer = new HessianSerializer();

    @Override
    protected void decode(ChannelHandlerContext ctx, ProtocolBody msg, List<Object> out) {
        if (msg.getType() == ProtocolHeader.REQUEST) {
            TinyRpcRequest tinyRpcRequest = serializer.deserialize(msg.getBody(), TinyRpcRequest.class);

            out.add(tinyRpcRequest);
        }
    }
}

package com.ndrlslz.tiny.rpc.client.codec;

import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import static com.ndrlslz.tiny.rpc.core.serialization.HessianSerializer.HESSIAN_SERIALIZER;

public class TinyRpcResponseDecoder extends MessageToMessageDecoder<ProtocolBody> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ProtocolBody msg, List<Object> out) {
        if (msg.getType() == ProtocolHeader.RESPONSE) {
            TinyRpcResponse tinyRpcResponse = HESSIAN_SERIALIZER.deserialize(msg.getBody(), TinyRpcResponse.class);

            out.add(tinyRpcResponse);
        }
    }
}
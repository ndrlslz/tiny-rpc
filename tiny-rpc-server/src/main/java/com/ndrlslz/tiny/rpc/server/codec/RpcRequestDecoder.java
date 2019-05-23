package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.server.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import static com.ndrlslz.tiny.rpc.server.serialization.HessianSerializer.HESSIAN_SERIALIZER;

public class RpcRequestDecoder extends MessageToMessageDecoder<ProtocolBody> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ProtocolBody msg, List<Object> out) {
        if (msg.getType() == ProtocolHeader.REQUEST) {
            TinyRpcRequest tinyRpcRequest = HESSIAN_SERIALIZER.deserialize(msg.getBody(), TinyRpcRequest.class);

            out.add(tinyRpcRequest);
        }
    }
}

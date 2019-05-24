package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static com.ndrlslz.tiny.rpc.core.serialization.HessianSerializer.HESSIAN_SERIALIZER;

public class RpcResponseEncoder extends MessageToMessageEncoder<TinyRpcResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TinyRpcResponse msg, List<Object> out) {
        ProtocolBody protocolBody = new ProtocolBody();
        protocolBody.setType(ProtocolHeader.RESPONSE);
        protocolBody.setBody(HESSIAN_SERIALIZER.serialize(msg));

        out.add(protocolBody);
    }
}
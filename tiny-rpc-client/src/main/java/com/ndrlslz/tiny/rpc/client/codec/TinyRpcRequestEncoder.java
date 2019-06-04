package com.ndrlslz.tiny.rpc.client.codec;

import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static com.ndrlslz.tiny.rpc.core.serialization.HessianSerializer.HESSIAN_SERIALIZER;

public class TinyRpcRequestEncoder extends MessageToMessageEncoder<TinyRpcRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TinyRpcRequest msg, List<Object> out) {
        ProtocolBody protocolBody = new ProtocolBody();
        protocolBody.setType(ProtocolHeader.REQUEST);
        protocolBody.setBody(HESSIAN_SERIALIZER.serialize(msg));

        out.add(protocolBody);
    }
}

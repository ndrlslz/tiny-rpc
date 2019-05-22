package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.server.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
import com.ndrlslz.tiny.rpc.server.serialization.HessianSerializer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class RpcResponseEncoder extends MessageToMessageEncoder<TinyRpcResponse> {
    private static final HessianSerializer serializer = new HessianSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, TinyRpcResponse msg, List<Object> out) {
        ProtocolBody protocolBody = new ProtocolBody();
        protocolBody.setType(ProtocolHeader.RESPONSE);
        protocolBody.setBody(serializer.serialize(msg));

        out.add(protocolBody);
    }
}
package com.ndrlslz.tiny.rpc.client.codec;

import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcHeartbeat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static com.ndrlslz.tiny.rpc.core.serialization.HessianSerializer.HESSIAN_SERIALIZER;

public class TinyRpcHeartbeatEncoder extends MessageToMessageEncoder<TinyRpcHeartbeat> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TinyRpcHeartbeat msg, List<Object> out) {
        ProtocolBody protocolBody = new ProtocolBody();
        protocolBody.setType(ProtocolHeader.HEARTBEAT);
        protocolBody.setBody(HESSIAN_SERIALIZER.serialize(msg));

        out.add(protocolBody);
    }
}

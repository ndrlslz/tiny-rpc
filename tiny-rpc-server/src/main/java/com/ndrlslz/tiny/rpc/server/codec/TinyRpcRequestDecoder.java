package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ndrlslz.tiny.rpc.core.serialization.HessianSerializer.HESSIAN_SERIALIZER;

public class TinyRpcRequestDecoder extends MessageToMessageDecoder<ProtocolBody> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcRequestDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ProtocolBody msg, List<Object> out) {
        if (msg.getType() == ProtocolHeader.REQUEST) {
            TinyRpcRequest tinyRpcRequest = HESSIAN_SERIALIZER.deserialize(msg.getBody(), TinyRpcRequest.class);

            out.add(tinyRpcRequest);
        } else if (msg.getType() == ProtocolHeader.HEARTBEAT) {
            LOGGER.info("receive heart beat message");
        }
    }
}

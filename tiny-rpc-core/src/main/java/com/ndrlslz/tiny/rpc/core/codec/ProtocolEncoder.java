package com.ndrlslz.tiny.rpc.core.codec;

import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader.MAGIC_NUMBER;

public class ProtocolEncoder extends MessageToByteEncoder<ProtocolBody> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolBody msg, ByteBuf out) {
        out.writeShort(MAGIC_NUMBER);
        out.writeByte(msg.getType());
        out.writeInt(msg.getBody().length);
        out.writeBytes(msg.getBody());
    }
}

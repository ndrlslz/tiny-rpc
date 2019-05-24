package com.ndrlslz.tiny.rpc.core.codec;

import com.ndrlslz.tiny.rpc.core.exception.MagicNumberNotCorrectException;
import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

import static com.ndrlslz.tiny.rpc.core.codec.State.*;
import static com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader.MAGIC_NUMBER;

public class ProtocolDecoder extends ReplayingDecoder<State> {
    private ProtocolHeader header = new ProtocolHeader();

    public ProtocolDecoder() {
        state(MAGIC);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        switch (state()) {
            case MAGIC:
                checkMagicNumber(in.readShort());
                checkpoint(TYPE);
            case TYPE:
                header.setType(in.readByte());
                checkpoint(BODY_LENGTH);
            case BODY_LENGTH:
                header.setBodyLength(in.readInt());
                checkpoint(BODY);
            case BODY:
                ByteBuf byteBuf = in.readBytes(header.getBodyLength());

                ProtocolBody protocolBody = new ProtocolBody();
                byte[] bytes = new byte[header.getBodyLength()];
                byteBuf.readBytes(bytes);

                protocolBody.setBody(bytes);
                protocolBody.setType(header.getType());
                out.add(protocolBody);
            default:
                checkpoint(MAGIC);
        }
    }


    private void checkMagicNumber(short magicNumber) {
        if (magicNumber != MAGIC_NUMBER) {
            throw new MagicNumberNotCorrectException("Magic number is not correct");
        }
    }
}
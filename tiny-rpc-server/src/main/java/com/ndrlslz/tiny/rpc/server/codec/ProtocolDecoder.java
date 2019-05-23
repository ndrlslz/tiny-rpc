package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.exception.MagicNumberNotCorrectException;
import com.ndrlslz.tiny.rpc.server.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.server.protocol.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

import static com.ndrlslz.tiny.rpc.server.codec.State.MAGIC;
import static com.ndrlslz.tiny.rpc.server.protocol.ProtocolHeader.MAGIC_NUMBER;

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
                checkpoint(State.TYPE);
            case TYPE:
                header.setType(in.readByte());
                checkpoint(State.BODY_LENGTH);
            case BODY_LENGTH:
                header.setBodyLength(in.readInt());
                checkpoint(State.BODY);
            case BODY:
                ByteBuf byteBuf = in.readBytes(header.getBodyLength());

                ProtocolBody protocolBody = new ProtocolBody();
                byte[] bytes = new byte[header.getBodyLength()];
                byteBuf.readBytes(bytes);

                protocolBody.setBody(bytes);
                protocolBody.setType(header.getType());
                out.add(protocolBody);
                break;
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
package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.protocol.RpcMessage;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.server.serialization.HessianSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

//@Sharable
public class RequestPacketCodec extends ByteToMessageCodec<RpcMessage<TinyRpcRequest>> {
    static final int BODY_LENGTH_SIZE = 4;
    private HessianSerializer serializer = new HessianSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage<TinyRpcRequest> msg, ByteBuf out) {
        TinyRpcRequest tinyRpcRequest = msg.getBody();

        byte[] bytes = serializer.serialize(tinyRpcRequest);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < BODY_LENGTH_SIZE) {
            return;
        }

        in.markReaderIndex();
        int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        ByteBuf byteBuf = in.readBytes(length);

        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        TinyRpcRequest tinyRpcRequest = (TinyRpcRequest) serializer.deserialize(bytes);

        RpcMessage<TinyRpcRequest> result = new RpcMessage<>();
        result.setBody(tinyRpcRequest);

        out.add(result);
    }
}
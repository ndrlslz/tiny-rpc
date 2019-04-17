package com.ndrlslz.tiny.rpc.server.codec;

import com.ndrlslz.tiny.rpc.server.protocol.RpcMessage;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.server.protocol.TinyRpcResponse;
import com.ndrlslz.tiny.rpc.server.serialization.HessianSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

import static com.ndrlslz.tiny.rpc.server.codec.RequestPacketCodec.BODY_LENGTH_SIZE;

public class ResponsePacketCodec extends ByteToMessageCodec<RpcMessage<TinyRpcResponse>> {
    private HessianSerializer serializer = new HessianSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcMessage<TinyRpcResponse> msg, ByteBuf out) {
        TinyRpcResponse tinyRpcResponse = msg.getBody();

        byte[] bytes = serializer.serialize(tinyRpcResponse);
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

        TinyRpcResponse tinyRpcResponse = (TinyRpcResponse) serializer.deserialize(bytes);

        RpcMessage<TinyRpcResponse> result = new RpcMessage<>();
        result.setBody(tinyRpcResponse);

        out.add(result);
    }
}

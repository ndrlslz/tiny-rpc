package com.ndrlslz.tiny.rpc.server.codec;


import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader.HEARTBEAT;
import static com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader.REQUEST;
import static com.ndrlslz.tiny.rpc.core.serialization.HessianSerializer.HESSIAN_SERIALIZER;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class TinyRpcRequestDecoderTest {
    private TinyRpcRequestDecoder tinyRpcRequestDecoder;

    @Before
    public void setUp() {
        tinyRpcRequestDecoder = new TinyRpcRequestDecoder();
    }

    @Test
    public void should_decode_tiny_rpc_request_when_receive__request() {
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        ProtocolBody protocolBody = new ProtocolBody();
        ArrayList<Object> out = new ArrayList<>();

        protocolBody.setType(REQUEST);
        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();
        tinyRpcRequest.setMethodName("test");
        protocolBody.setBody(HESSIAN_SERIALIZER.serialize(tinyRpcRequest));

        tinyRpcRequestDecoder.decode(ctx, protocolBody, out);

        assertThat(out.size(), is(1));
        assertThat(out.get(0), instanceOf(TinyRpcRequest.class));
    }

    @Test
    public void should_do_nothing_when_receive_heartbeat_message() {
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        ProtocolBody protocolBody = new ProtocolBody();
        protocolBody.setType(HEARTBEAT);
        ArrayList<Object> out = new ArrayList<>();

        tinyRpcRequestDecoder.decode(ctx, protocolBody, out);

        assertThat(out.size(), is(0));
    }
}
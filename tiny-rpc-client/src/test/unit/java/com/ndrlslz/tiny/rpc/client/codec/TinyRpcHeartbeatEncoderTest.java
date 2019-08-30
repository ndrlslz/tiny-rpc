package com.ndrlslz.tiny.rpc.client.codec;

import com.ndrlslz.tiny.rpc.core.protocol.ProtocolBody;
import com.ndrlslz.tiny.rpc.core.protocol.ProtocolHeader;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcHeartbeat;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;

import static com.ndrlslz.tiny.rpc.core.serialization.HessianSerializer.HESSIAN_SERIALIZER;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class TinyRpcHeartbeatEncoderTest {
    @InjectMocks
    private TinyRpcHeartbeatEncoder tinyRpcHeartbeatEncoder;

    @Mock
    private ChannelHandlerContext ctx;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void should_encode_tiny_rpc_heartbeat() {
        TinyRpcHeartbeat tinyRpcHeartbeat = new TinyRpcHeartbeat();
        ArrayList<Object> out = new ArrayList<>();

        tinyRpcHeartbeatEncoder.encode(ctx, tinyRpcHeartbeat, out);

        assertThat(out.size(), is(1));
        assertThat(out.get(0), instanceOf(ProtocolBody.class));
        ProtocolBody protocolBody = (ProtocolBody) out.get(0);
        assertThat(protocolBody.getType(), is(ProtocolHeader.HEARTBEAT));
        assertThat(protocolBody.getBody(), is(HESSIAN_SERIALIZER.serialize(tinyRpcHeartbeat)));
    }
}
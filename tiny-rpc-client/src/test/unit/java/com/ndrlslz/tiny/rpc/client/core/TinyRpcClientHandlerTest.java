package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.client.callback.MessageCallback;
import com.ndrlslz.tiny.rpc.client.callback.MessageCallbackStorage;
import com.ndrlslz.tiny.rpc.client.pool.ConnectionPool;
import com.ndrlslz.tiny.rpc.core.exception.TinyRpcException;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcHeartbeat;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.Field;

import static io.netty.handler.timeout.IdleStateEvent.WRITER_IDLE_STATE_EVENT;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TinyRpcClientHandlerTest {
    @InjectMocks
    private TinyRpcClientHandler tinyRpcClientHandler;

    @Mock
    private ConnectionPool connectionPool;

    @Mock
    private ChannelHandlerContext ctx;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void should_return_tiny_rpc_exception_when_catch_exception_given_correlation_id_exists() throws InterruptedException {
        MessageCallback messageCallback = new MessageCallback();
        String correlationId = "correlation-id";
        MessageCallbackStorage.put(correlationId, messageCallback);
        setUpCorrelationId(correlationId);

        tinyRpcClientHandler.exceptionCaught(ctx, new RuntimeException());

        assertThat(MessageCallbackStorage.get(correlationId), nullValue());
        assertThat(messageCallback.get(0), instanceOf(TinyRpcException.class));
    }

    @Test
    public void should_send_heartbeat_when_write_idle_event_triggered() throws Exception {
        when(ctx.writeAndFlush(any())).thenReturn(mock(ChannelFuture.class));

        tinyRpcClientHandler.userEventTriggered(ctx, WRITER_IDLE_STATE_EVENT);

        verify(ctx, times(1)).writeAndFlush(any(TinyRpcHeartbeat.class));
    }

    private void setUpCorrelationId(String correlationId) {
        try {
            Field field = TinyRpcClientHandler.class.getDeclaredField("correlationId");
            field.setAccessible(true);

            field.set(tinyRpcClientHandler, correlationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
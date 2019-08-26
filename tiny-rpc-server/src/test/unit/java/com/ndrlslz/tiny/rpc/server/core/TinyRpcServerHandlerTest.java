package com.ndrlslz.tiny.rpc.server.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TinyRpcServerHandlerTest {
    @InjectMocks
    private TinyRpcServerHandler tinyRpcServerHandler;

    @Mock
    private TinyRpcRequestHandler tinyRpcRequestHandler;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void should_close_channel_when_reader_idle_event_triggered() throws Exception {
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        Channel channel = mock(Channel.class);

        when(ctx.channel()).thenReturn(channel);
        when(channel.close()).thenReturn(mock(ChannelFuture.class));

        tinyRpcServerHandler.userEventTriggered(ctx, IdleStateEvent.READER_IDLE_STATE_EVENT);

        verify(ctx, times(1)).channel();
        verify(channel, times(1)).close();
    }

    @Test
    public void should_do_nothing_when_write_idle_event_triggered() throws Exception {
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        Channel channel = mock(Channel.class);

        when(ctx.channel()).thenReturn(channel);

        tinyRpcServerHandler.userEventTriggered(ctx, IdleStateEvent.WRITER_IDLE_STATE_EVENT);

        verifyNoMoreInteractions(ctx);
    }
}
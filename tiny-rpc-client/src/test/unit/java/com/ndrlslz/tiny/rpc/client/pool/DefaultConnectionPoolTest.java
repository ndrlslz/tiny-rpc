package com.ndrlslz.tiny.rpc.client.pool;

import com.ndrlslz.tiny.rpc.service.core.TinyRpcServiceOptions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.*;

public class DefaultConnectionPoolTest {
    private DefaultConnectionPool connectionPool;
    private Bootstrap bootstrap;
    private String host;
    private int port;

    @Before
    public void setUp() {
        TinyRpcServiceOptions serviceOptions = new TinyRpcServiceOptions();
        bootstrap = Mockito.mock(Bootstrap.class, RETURNS_DEEP_STUBS);
        host = "127.0.0.1";
        port = 9999;
        connectionPool = new DefaultConnectionPool(serviceOptions, bootstrap, host, port);
    }


    @Test
    public void should_create_new_connection() throws InterruptedException {
        ChannelFuture channelFuture = mock(ChannelFuture.class);
        when(bootstrap.connect(host, port)).thenReturn(channelFuture);
        when(channelFuture.sync()).thenReturn(channelFuture);

        connectionPool.createConnection();

        verify(bootstrap).connect(host, port);
    }

    @Test
    public void should_borrow_connection() throws InterruptedException {
        ChannelFuture channelFuture = mock(ChannelFuture.class);
        Channel channel = mock(Channel.class);
        when(bootstrap.connect(host, port)).thenReturn(channelFuture);
        when(channelFuture.sync()).thenReturn(channelFuture);
        when(channelFuture.channel()).thenReturn(channel);
        when(channel.isActive()).thenReturn(true);

        PooledConnection connection = connectionPool.borrowConnection();

        assertThat(connection, is(notNullValue()));
        verify(bootstrap).connect(host, port);
    }
}
package com.ndrlslz.tiny.rpc.client.core;

import com.ndrlslz.tiny.rpc.client.callback.MessageCallback;
import com.ndrlslz.tiny.rpc.client.callback.MessageCallbackStorage;
import com.ndrlslz.tiny.rpc.client.exception.TinyRpcTimeoutException;
import com.ndrlslz.tiny.rpc.client.model.NullObject;
import com.ndrlslz.tiny.rpc.client.pool.DefaultConnectionPool;
import com.ndrlslz.tiny.rpc.client.pool.PooledConnection;
import com.ndrlslz.tiny.rpc.core.protocol.TinyRpcRequest;
import com.ndrlslz.tiny.rpc.service.core.TinyRpcServiceOptions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static java.lang.String.format;
import static java.util.Objects.isNull;

public class TinyRpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TinyRpcClient.class);
    private String host;
    private int port;
    private NioEventLoopGroup workerGroup;
    private final Bootstrap bootstrap;
    private TinyRpcServiceOptions tinyRpcServiceOptions;
    private DefaultConnectionPool defaultConnectionPool;

    public TinyRpcClient(String host, int port, TinyRpcServiceOptions tinyRpcServiceOptions) {
        this.tinyRpcServiceOptions = isNull(tinyRpcServiceOptions) ? new TinyRpcServiceOptions() : tinyRpcServiceOptions;

        workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        this.defaultConnectionPool = new DefaultConnectionPool(bootstrap, host, port, 10, 100);

        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new TinyRpcClientInitializer(defaultConnectionPool));
                    }
                });

        this.host = host;
        this.port = port;

        defaultConnectionPool.InitConnections();
    }

    public Object invoke(String method, Object[] parameters) throws InterruptedException {
//        defaultConnectionPool.printConnections();
        TinyRpcRequest tinyRpcRequest = buildTinyRpcRequest(method, parameters);

        MessageCallback messageCallback = new MessageCallback();
        MessageCallbackStorage.put(tinyRpcRequest.getCorrelationId(), messageCallback);

        PooledConnection connection = defaultConnectionPool.borrowConnection();
        connection.writeAndFlush(tinyRpcRequest);

        Object result = messageCallback.get(tinyRpcServiceOptions.getTimeout());

        defaultConnectionPool.surrenderConnection(connection);

        if (isNull(result)) {
            return new TinyRpcTimeoutException(format("Timeout exception, cannot get response within %s seconds",
                    tinyRpcServiceOptions.getTimeout()));
        }

        if (result instanceof NullObject) {
            return null;
        } else {
            return result;
        }
    }

    private TinyRpcRequest buildTinyRpcRequest(String method, Object[] parameters) {
        TinyRpcRequest tinyRpcRequest = new TinyRpcRequest();
        tinyRpcRequest.setMethodName(method);
        tinyRpcRequest.setArgumentsValue(parameters);
        //TODO maybe snow id?
        tinyRpcRequest.setCorrelationId(UUID.randomUUID().toString());
        return tinyRpcRequest;
    }

    public void close() {
        defaultConnectionPool.close();
        workerGroup.shutdownGracefully();
    }
}
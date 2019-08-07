package com.ndrlslz.tiny.rpc.client.pool;

import com.ndrlslz.tiny.rpc.client.exception.TinyRpcNoAvailableConnectionException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class DefaultConnectionPool implements ConnectionPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConnectionPool.class);
    private volatile AtomicInteger currentConnectionCount;
    private LinkedBlockingQueue<PooledConnection> availableConnections;
    private CopyOnWriteArrayList<PooledConnection> usedConnections;
    private int minCount;
    private int maxCount;
    private Bootstrap bootstrap;
    private String host;
    private int port;

    public DefaultConnectionPool(Bootstrap bootstrap, String host, int port, int minCount, int maxCount) {
        this.bootstrap = bootstrap;
        this.host = host;
        this.port = port;
        this.minCount = minCount;
        this.maxCount = maxCount;
        availableConnections = new LinkedBlockingQueue<>(maxCount);
        usedConnections = new CopyOnWriteArrayList<>();
        currentConnectionCount = new AtomicInteger();
    }

    public void printConnections() {
        int usedSize = usedConnections.size();
        int availableSize = availableConnections.size();
        int total = currentConnectionCount.get();

        assert total == availableSize + usedSize;
        System.out.println("total: " + total + " available size: " + availableSize + " usedSize: " + usedSize);
    }

    @Override
    public void InitConnections() {
        IntStream.range(0, minCount).forEach(value -> {
            try {
                ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                availableConnections.offer(new PooledConnection(channelFuture.channel()));
            } catch (InterruptedException exception) {
                LOGGER.error("Failed to create new channel", exception);
            }
        });

        currentConnectionCount.set(minCount);
    }

    @Override
    public synchronized void createConnection() {
        if (currentConnectionCount.get() < maxCount) {
            try {
                ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                PooledConnection pooledConnection = new PooledConnection(channelFuture.channel());
                availableConnections.offer(pooledConnection);
                currentConnectionCount.incrementAndGet();
            } catch (InterruptedException exception) {
                LOGGER.error("Failed to create new channel", exception);
            }
        }
    }

    @Override
    public PooledConnection borrowConnection() throws InterruptedException {
        if (currentConnectionCount.get() < minCount) {
            createConnection();
        }

        if (availableConnections.isEmpty()) {
            if (currentConnectionCount.get() < maxCount) {
                createConnection();
            }

            return getActiveConnection();
        }

        return getActiveConnection();
    }

    private PooledConnection getActiveConnection() throws InterruptedException {
        PooledConnection connection = availableConnections.poll(10, TimeUnit.SECONDS);
        if (connection == null) {
            throw new TinyRpcNoAvailableConnectionException("No available connection can be used, and cannot create new connection as well");
        }

        if (!connection.isActive()) {
            removeConnection(connection);
            return getActiveConnection();
        } else {
            usedConnections.add(connection);
            return connection;
        }
    }

    @Override
    public void surrenderConnection(PooledConnection connection) {
        usedConnections.remove(connection);
        availableConnections.offer(connection);
    }

    @Override
    public void removeConnection(PooledConnection connection) {
        availableConnections.remove(connection);
        usedConnections.remove(connection);
        currentConnectionCount.decrementAndGet();
    }

    @Override
    public synchronized void close() {
        availableConnections.forEach(PooledConnection::close);
        usedConnections.forEach(PooledConnection::close);
    }
}
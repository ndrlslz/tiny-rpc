package com.ndrlslz.tiny.rpc.client.pool;

public interface ConnectionPool {
    void InitConnections();

    void createConnection();

    PooledConnection borrowConnection() throws InterruptedException;

    void surrenderConnection(PooledConnection connection);

    void removeConnection(PooledConnection connection);

    void close();
}

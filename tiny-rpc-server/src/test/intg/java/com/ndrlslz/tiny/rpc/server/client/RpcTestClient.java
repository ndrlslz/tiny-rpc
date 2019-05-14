package com.ndrlslz.tiny.rpc.server.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class RpcTestClient {
    private Socket clientSocket;

    public RpcTestClient(String address, int port) {
        try {
            clientSocket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRpcRequest(byte[] bytes) {
        DataOutputStream outToServer = null;

        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());

            outToServer.writeInt(bytes.length);
            outToServer.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outToServer != null) {
                    outToServer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] receiveRpcResponse() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            byte[] bodyLength = new byte[4];
            inputStream.read(bodyLength);

            int length = fromByteArray(bodyLength);
            byte[] buffer = new byte[length];
            inputStream.read(buffer);

            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

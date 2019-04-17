package com.ndrlslz.tiny.rpc.server.client;

import java.io.*;
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

    int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    public void send(String message) {
        DataOutputStream outToServer = null;

        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(message);
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

    public String receive() {
        try {
            byte[] buffer = new byte[1024];

            InputStream inputStream = clientSocket.getInputStream();

            OutputStream outputStream = new ByteArrayOutputStream();

            int count = inputStream.read(buffer);
            outputStream.write(buffer, 0, count);

            return outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

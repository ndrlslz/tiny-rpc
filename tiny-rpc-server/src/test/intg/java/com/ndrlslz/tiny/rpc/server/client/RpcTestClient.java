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

            outToServer.writeShort(0xbabe);
            outToServer.writeByte(0x01);
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

            byte[] magic = new byte[2];
            byte[] bodyLength = new byte[4];
            byte[] type = new byte[1];

            inputStream.read(magic);
            inputStream.read(type);
            inputStream.read(bodyLength);

            if (magic[0] != (byte) 0xba && magic[1] != (byte) 0xbe) {
                throw new RuntimeException("magic number not correct");
            }

            if (type[0] != (byte) 0x02) {
                throw new RuntimeException("expect response, but not a response");
            }

            int length = fromByteArray(bodyLength);
            byte[] response = new byte[length];
            inputStream.read(response);

            return response;
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

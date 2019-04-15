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

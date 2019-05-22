package com.ndrlslz.tiny.rpc.server.protocol;

public class ProtocolBody {
    private byte type;
    private byte[] body;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
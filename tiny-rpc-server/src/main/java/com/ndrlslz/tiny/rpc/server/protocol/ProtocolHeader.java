package com.ndrlslz.tiny.rpc.server.protocol;

public class ProtocolHeader {
    public static final byte REQUEST = 0x01;
    public static final byte RESPONSE = 0x02;
    public static final byte HEARTBEAT = 0x03;


    private short magicNumber;
    private byte type;
    private int bodyLength;

    public short getMagicNumber() {
        return magicNumber;
    }

    public void setMagicNumber(short magicNumber) {
        this.magicNumber = magicNumber;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }
}

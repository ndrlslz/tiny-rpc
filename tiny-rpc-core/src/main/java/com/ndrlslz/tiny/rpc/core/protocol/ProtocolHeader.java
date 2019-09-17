package com.ndrlslz.tiny.rpc.core.protocol;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProtocolHeader {
    public static final byte REQUEST = 0x01;
    public static final byte RESPONSE = 0x02;
    public static final byte HEARTBEAT = 0x03;
    public static final short MAGIC_NUMBER = (short) 0xbabe;

    private short magicNumber;
    private byte type;
    private int bodyLength;
}

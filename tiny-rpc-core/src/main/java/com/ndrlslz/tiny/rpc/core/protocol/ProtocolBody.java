package com.ndrlslz.tiny.rpc.core.protocol;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProtocolBody {
    private byte type;
    private byte[] body;
}
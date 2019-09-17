package com.ndrlslz.tiny.rpc.core.protocol;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TinyRpcRequest extends TinyRpcMessage implements Serializable {
    private String methodName;
    private Object[] argumentsValue;
}

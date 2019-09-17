package com.ndrlslz.tiny.rpc.core.protocol;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TinyRpcResponse extends TinyRpcMessage implements Serializable {
    private String methodName;
    private Class responseType;
    private Object responseValue;
}

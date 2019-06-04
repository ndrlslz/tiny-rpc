package com.ndrlslz.tiny.rpc.core.protocol;

import java.io.Serializable;

public class TinyRpcResponse extends TinyRpcMessage implements Serializable {
    private String methodName;
    private Class responseType;
    private Object responseValue;

    public String getMethodName() {
        return methodName;
    }

    public Class getResponseType() {
        return responseType;
    }

    public Object getResponseValue() {
        return responseValue;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setResponseType(Class responseType) {
        this.responseType = responseType;
    }

    public void setResponseValue(Object responseValue) {
        this.responseValue = responseValue;
    }

    @Override
    public String toString() {
        return "TinyRpcResponse{" +
                "methodName='" + methodName + '\'' +
                ", responseType=" + responseType +
                ", responseValue=" + responseValue +
                '}';
    }
}

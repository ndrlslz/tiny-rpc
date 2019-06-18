package com.ndrlslz.tiny.rpc.core;

import java.io.Serializable;

public class Output implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.ndrlslz.tiny.rpc.server;

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

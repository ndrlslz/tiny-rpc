package com.ndrlslz.tiny.rpc.core;

import java.io.Serializable;

public class Input implements Serializable {
    private String name;
    private Details details;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }
}

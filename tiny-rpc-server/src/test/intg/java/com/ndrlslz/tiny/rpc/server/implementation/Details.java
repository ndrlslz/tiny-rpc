package com.ndrlslz.tiny.rpc.server.implementation;

import java.io.Serializable;

public class Details implements Serializable {
    private String address;
    private int age;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

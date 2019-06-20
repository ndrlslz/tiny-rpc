package com.ndrlslz.tiny.rpc.service;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Sample {
    @Test
    public void alwaysTrue() {
        assertThat(true, is(true));
    }
}

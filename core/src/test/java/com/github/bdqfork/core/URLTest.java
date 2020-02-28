package com.github.bdqfork.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URLTest {

    @Test
    void toPath() {
        URL url = new URL("rpc", "127.0.0.1", 8080, "com.test.IService");
        url.addParam("group", "dev");
        assertEquals("rpc://127.0.0.1:8080/com.test.IService?group=dev", url.toPath());
    }
}
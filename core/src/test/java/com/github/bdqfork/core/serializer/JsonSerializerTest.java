package com.github.bdqfork.core.serializer;

import com.github.bdqfork.core.exception.SerializerException;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonSerializerTest {

    @Test
    void serialize() throws SerializerException {
        Foo foo = new JsonSerializerTest.Foo();
        foo.id = 100000000000L;
        byte[] bytes = new JsonSerializer().serialize(foo);
        String json = "{\"id\":\"100000000000\"}";
        assertEquals(new String(bytes, StandardCharsets.UTF_8), json);
    }

    @Test
    void deserialize() throws SerializerException {
        Foo foo = new JsonSerializerTest.Foo();
        foo.id = 100000000000L;
        Serializer serializer = new JsonSerializer();
        byte[] bytes = serializer.serialize(foo);
        Foo foo1 = serializer.deserialize(bytes, Foo.class);
        assertEquals(foo1.id, 100000000000L);
    }

    static class Foo implements Serializable {
        private long id;
    }
}

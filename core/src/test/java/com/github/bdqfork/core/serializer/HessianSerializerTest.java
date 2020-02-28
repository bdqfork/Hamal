package com.github.bdqfork.core.serializer;

import com.github.bdqfork.core.exception.SerializerException;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class HessianSerializerTest implements Serializable {

    @Test
    void serialize() throws SerializerException {
        Foo foo = new Foo();
        foo.id = 100;
        byte[] bytes = new HessianSerializer().serialize(foo);
        assertNotEquals(bytes.length,0);
    }

    @Test
    void deserialize() throws SerializerException {
        Foo foo = new Foo();
        foo.id = 100;
        Serializer serializer = new HessianSerializer();
        byte[] bytes = serializer.serialize(foo);
        Foo foo1 = serializer.deserialize(bytes, Foo.class);
        assertEquals(foo1.id, 100);
    }

    static class Foo implements Serializable {
        int id;
    }
}
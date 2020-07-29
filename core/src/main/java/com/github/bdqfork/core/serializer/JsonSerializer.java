package com.github.bdqfork.core.serializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.bdqfork.core.exception.SerializerException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JsonSerializer implements Serializer{

    private final ObjectMapper objectMapper = new  ObjectMapper();
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
    }


    @Override
    public byte[] serialize(Serializable serializable) throws SerializerException {
        try {
            String jsonString = objectMapper.writeValueAsString(serializable);
            return jsonString.getBytes(CHARSET);
        } catch (JsonProcessingException e) {
            throw new SerializerException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws SerializerException {
        String jsonString = new String(bytes, CHARSET);
        Reader reader = new StringReader(jsonString);
        try {
            return objectMapper.readValue(reader, type);
        } catch (IOException e) {
            throw new SerializerException(e);
        }
    }
}

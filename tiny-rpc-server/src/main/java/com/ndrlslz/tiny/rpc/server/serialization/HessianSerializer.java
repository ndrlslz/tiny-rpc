package com.ndrlslz.tiny.rpc.server.serialization;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HessianSerializer.class);
    public static final Serializer HESSIAN_SERIALIZER = new HessianSerializer();

    public <T> byte[] serialize(T object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);

        try {
            hessian2Output.writeObject(object);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                hessian2Output.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return byteArrayOutputStream.toByteArray();
    }

    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);

        try {
            return clazz.cast(hessian2Input.readObject());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                hessian2Input.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return null;
    }
}
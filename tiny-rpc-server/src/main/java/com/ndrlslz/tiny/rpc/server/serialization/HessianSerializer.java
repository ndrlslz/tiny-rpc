package com.ndrlslz.tiny.rpc.server.serialization;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer implements Serializer {
    public byte[] serialize(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);

        try {
            hessian2Output.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                hessian2Output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return byteArrayOutputStream.toByteArray();
    }

    public Object deserialize(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);

        try {
            return hessian2Input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                hessian2Input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}

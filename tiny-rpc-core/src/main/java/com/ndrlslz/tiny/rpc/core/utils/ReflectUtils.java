package com.ndrlslz.tiny.rpc.core.utils;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class ReflectUtils {
    public static String getCodeBase(Class<?> cls) {
        if (cls == null) {
            return null;
        } else {
            ProtectionDomain domain = cls.getProtectionDomain();
            if (domain == null) {
                return null;
            } else {
                CodeSource source = domain.getCodeSource();
                if (source == null) {
                    return null;
                } else {
                    URL location = source.getLocation();
                    return location == null ? null : location.getFile();
                }
            }
        }
    }
}
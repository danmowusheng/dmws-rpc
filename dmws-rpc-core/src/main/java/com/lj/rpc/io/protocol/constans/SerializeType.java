package com.lj.rpc.io.protocol.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SerializeType {
    PROTOSTUFF((byte) 1, "protostuff"),

    KRYO((byte) 2, "kryo"),

    HESSIAN((byte) 3, "hessian")
    ;

    private final byte value;
    private final String name;


    public static SerializeType fromValue(byte value) {
        for (SerializeType serializeType : SerializeType.values()) {
            if (serializeType.getValue() == value) {
                return serializeType;
            }
        }
        return null;
    }

    public static SerializeType fromName(String name) {
        for (SerializeType serializeType : SerializeType.values()) {
            if (serializeType.getName().equals(name)) {
                return serializeType;
            }
        }
        return null;
    }
}

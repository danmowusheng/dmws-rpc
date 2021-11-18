package com.lj.rpc.io.protocol.constans;

public enum SerializeType {
    PROTOSTUFF((byte) 1, "protostuff"),

    KRYO((byte) 2, "kryo"),

    HESSIAN((byte) 3, "hessian")
    ;

    private final byte value;
    private final String name;

    SerializeType(byte value, String name) {
        this.value = value;
        this.name = name;
    }
}

package com.lj.rpc.io.protocol.constans;

public enum CompressType {
    /**
     * 伪压缩器，不做压缩，有一些序列化工具压缩已经做的很好了，这里不用压缩也可以
     */
    DUMMY((byte) 0, "dummy"),

    GZIP((byte) 1, "gzip");

    private final byte value;
    private final String name;

    CompressType(byte value, String name) {
        this.value = value;
        this.name = name;
    }
}

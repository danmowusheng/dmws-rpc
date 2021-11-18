package com.lj.rpc.io.protocol.constans;

public enum MessageType {
    /**
     * 请求
     */
    REQUEST((byte) 1),

    /**
     * 响应
     */
    RESPONSE((byte) 2),

    /**
     * 心跳
     */
    HEARTBEAT((byte) 3);

    private final byte value;

    MessageType(byte value) {
        this.value = value;
    }
}

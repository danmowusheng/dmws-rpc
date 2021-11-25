package com.lj.rpc.io.protocol.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}

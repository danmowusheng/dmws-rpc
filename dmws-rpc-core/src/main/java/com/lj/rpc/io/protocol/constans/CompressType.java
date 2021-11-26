package com.lj.rpc.io.protocol.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompressType {
    /**
     * 伪压缩器，不做压缩，有一些序列化工具压缩已经做的很好了，这里不用压缩也可以
     */
    DUMMY((byte) 0, "dummy"),

    GZIP((byte) 1, "gzip");

    private final byte value;
    private final String name;

    /**
     * 通过值获取压缩类型枚举
     *
     * @param value 值
     * @return 如果获取不到，返回 DUMMY
     */
    public static CompressType fromValue(byte value) {
        for (CompressType codecType : CompressType.values()) {
            if (codecType.getValue() == value) {
                return codecType;
            }
        }
        return DUMMY;
    }

    /**
     * 通过名字获取压缩类型枚举
     *
     * @param name 名字
     * @return 如果获取不到，返回 DUMMY
     */
    public static CompressType fromName(String name) {
        for (CompressType codecType : CompressType.values()) {
            if (codecType.getName().equals(name)) {
                return codecType;
            }
        }
        return DUMMY;
    }
}

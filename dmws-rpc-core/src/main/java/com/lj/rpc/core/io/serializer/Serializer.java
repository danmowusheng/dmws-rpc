package com.lj.rpc.core.io.serializer;

/**
 * 序列化器
 */
public interface Serializer {
    /**
     * 序列化
     * @param object
     * @return 字节数组
     */
    byte[] serialize(Object object);

    /**
     * 反序列化
     * @param bytes 字节数组
     * @param clazz 要反序列化的类
     * @param <T> 返回对象类型
     * @return 返回对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}

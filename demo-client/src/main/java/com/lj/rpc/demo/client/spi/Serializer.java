package com.lj.rpc.demo.client.spi;

import com.lj.rpc.common.extension.SPI;

/**
 * 客户端序列化接口
 */
@SPI
public interface Serializer {
    byte[] serialize(Object object);
}

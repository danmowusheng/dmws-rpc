package com.dmws.rpc.demo.client.spi;

import com.dmws.rpc.common.extension.SPI;

/**
 * 客户端序列化接口
 */
@SPI
public interface Serializer {
    byte[] serialize(Object object);
}

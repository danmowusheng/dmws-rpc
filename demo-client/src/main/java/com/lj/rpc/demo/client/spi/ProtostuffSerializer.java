package com.lj.rpc.demo.client.spi;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 15:15
 * @description：
 **/
public class ProtostuffSerializer implements Serializer {
    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    @Override
    public byte[] serialize(Object object) {
        Schema schema = RuntimeSchema.getSchema(object.getClass());
        return ProtostuffIOUtil.toByteArray(object, schema, BUFFER);
    }
}

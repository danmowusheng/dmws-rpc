package com.lj.rpc.io.serializer.protostuff;

import com.lj.rpc.io.serializer.Serializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-24 16:04
 * @description： https://github.com/protostuff/protostuff
 **/
public class protoStuffSerializer implements Serializer {

    private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] serialize(Object object) {
        Schema schema = RuntimeSchema.getSchema(object.getClass());
        try {
            return ProtostuffIOUtil.toByteArray(object, schema, BUFFER);
        }finally {
            BUFFER.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
}

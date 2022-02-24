package com.dmws.rpc.core.config;

import com.dmws.rpc.core.annotation.Config;
import com.dmws.rpc.core.io.protocol.constans.CompressType;
import com.dmws.rpc.core.io.protocol.constans.SerializeType;
import lombok.Data;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 15:59
 * @description： 协议相关配置
 **/
@Data
@Config(prefix = "protocol")
public class ProtocolConfig {
    /**
     * 序列化类型{@link SerializeType}
     */
    private String serializeType;

    /**
     * 压缩类型{@link CompressType}
     */
    private String compressType;

    public String getSerializeType(){
        return serializeType == null ? SerializeType.PROTOSTUFF.getName() : serializeType;
    }

    public String getCompressType(){
        return compressType == null ? CompressType.DUMMY.getName() : compressType;
    }
}

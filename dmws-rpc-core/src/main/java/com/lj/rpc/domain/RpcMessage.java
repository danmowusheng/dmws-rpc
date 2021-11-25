package com.lj.rpc.domain;

import com.lj.rpc.io.protocol.constans.CompressType;
import com.lj.rpc.io.protocol.constans.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-19 15:36
 * @description： Rpc解码前的通用格式
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcMessage {
    /**
     * 消息类型{@link MessageType#getValue()}
     */
    private byte messageType;

    /**
     * 压缩类型{@link CompressType#getValue()}
     */
    private byte compressType;

    /**
     * 序列化类型
     */
    private byte serializerType;

    /**
     * 消息ID
     */
    private long messageID;

    /**
     * 消息体
     */
    private Object data;
}

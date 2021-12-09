package com.lj.rpc.core.io.protocol.constans;


import java.util.concurrent.atomic.AtomicLong;



/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-18 18:23
 * @description： 消息格式常量
 **/
public class MessageConstants {
    /**
     * 魔数,定义5个字节
     */
    public static final byte[] MAGIC_NUMBER = {(byte)'l', (byte)'j',(byte)'r',(byte)'p',(byte)'c',};

    /**
     * 版本号,1个字节
     */
    public static final byte VERSION = 1;

    /**
     * 消息ID
     */
    public static AtomicLong MESSAGE_ID = new AtomicLong(0);

    public static final String PING = "ping";
    public static final String PONG = "pong";

    /**
     * 总长度字段的长度
     */
    public static final int FULL_LENGTH_LENGTH = 4;

    /**
     * 魔数长度
     */
    public static final int MAGIC_NUMBER_LENGTH = 5;

    /**
     * 版本长度
     */
    public static final int VERSION_LENGTH = 1;

    /**
     * 消息类型长度
     */
    public static final int MESSAGE_TYPE_LENGTH = 1;

    /**
     * 编解码类型长度
     */
    public static final int CODEC_LENGTH = 1;

    /**
     * 压缩器类型长度
     */
    public static final int COMPRESS_LENGTH = 1;

    /**
     * 请求id 长度
     */
    public static final int REQUEST_ID_LENGTH = 8;

    /**
     * 请求头长度
     */
    public static final int HEADER_LENGTH = MAGIC_NUMBER_LENGTH + VERSION_LENGTH + FULL_LENGTH_LENGTH + MESSAGE_TYPE_LENGTH + CODEC_LENGTH + CODEC_LENGTH + REQUEST_ID_LENGTH;

    /**
     * 协议最大长度
     */
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}

package com.lj.rpc.io.protocol;


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
    AtomicLong MESSAGE_ID = new AtomicLong(0);

    String PING = "ping";
    String PONG = "pong";

}

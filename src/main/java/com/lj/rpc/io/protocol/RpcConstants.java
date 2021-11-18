package com.lj.rpc.io.protocol;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-18 18:23
 * @description： RPC通信协议
 **/
public class RpcConstants {
    public static final byte[] MAGIC_NUMBER = {(byte)'l', (byte)'j',(byte)'r',(byte)'p',(byte)'c',};
    /**
     * 选用UTF-8字符集
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static final byte VERSION = 1;
    public static final byte TOTAL_LENGTH = 16;
}

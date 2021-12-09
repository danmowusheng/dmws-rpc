package com.lj.rpc.core.io.compress;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-24 16:34
 * @description： 伪压缩器，什么也不做，因为有一些序列化协议做的已经很好了
 **/
public class DummyCompressor implements Compressor{
    @Override
    public byte[] compress(byte[] bytes) {
        return bytes;
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return bytes;
    }
}

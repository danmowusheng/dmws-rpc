package com.dmws.rpc.core.io.compress;

import com.dmws.rpc.common.extension.SPI;

/**
 * 压缩器
 */
@SPI("dummy")
public interface Compressor {
    /**
     * 压缩
     * @param bytes 压缩前字节数组
     * @return 压缩后字节数组
     */
    byte[] compress(byte[] bytes);

    /**
     * 解压
     * @param bytes 解压前字节数组
     * @return 解压后字节数组
     */
    byte[] decompress(byte[] bytes);

}

package com.lj.rpc.core.config.loader;

import com.lj.rpc.common.extension.SPI;

@SPI
public interface ConfigLoader {
    /**
     * 加载配置项
     * @param key 配置对应的key
     * @return 配置项的值，如果不存在，返回 null
     */
    String loadConfigItem(String key);
}

package com.dmws.rpc.core.config.loader;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 15:35
 * @description： java 参数配置 -Dprefix.configField=xxx
 **/
@Slf4j
public class SystemPropertyLoader implements ConfigLoader{
    @Override
    public String loadConfigItem(String key) {
        return System.getProperty(key);
    }
}

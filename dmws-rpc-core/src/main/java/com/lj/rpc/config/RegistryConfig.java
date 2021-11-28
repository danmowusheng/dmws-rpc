package com.lj.rpc.config;

import com.lj.rpc.annotation.Config;
import com.lj.rpc.common.url.URL;
import lombok.Data;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 15:18
 * @description： 注册中心配置
 **/
@Data
@Config(prefix = "registry")
public class RegistryConfig {
    /**
     * 注册中心地址。eg: zk://10.20.153.10:6379?backup=10.20.153.11:6379,10.20.153.12:6379
     */
    private String address;

    public URL toURL(){
        return URL.valueOf(address);
    }
}

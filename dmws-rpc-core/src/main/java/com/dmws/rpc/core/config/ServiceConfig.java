package com.dmws.rpc.core.config;

import com.dmws.rpc.core.annotation.Config;
import lombok.Data;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 15:23
 * @description： 服务启动配置
 **/
@Data
@Config(prefix = "service")
public class ServiceConfig {
    /**
     * 默认服务端口
     */
    private static final Integer DEFAULT_SERVER_PORT = 3000;

    /**
     * 监听端口
     */
    private Integer port;

    public Integer getPort(){
        return (port != null && port > 0) ? port : DEFAULT_SERVER_PORT;
    }
}

package com.lj.rpc.core.config;

import com.lj.rpc.core.annotation.Config;
import lombok.Data;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 16:04
 * @description： 集群相关配置
 **/
@Data
@Config(prefix = "cluster")
public class ClusterConfig {
    /**
     * 负载均衡策略
     */
    private String loadBalance;

    /**
     * 容错策略
     */
    private String faultTolerant;

    /**
     * 重试次数，只有容错策略是‘retry’时才有效
     */
    private Integer retryTimes;
}

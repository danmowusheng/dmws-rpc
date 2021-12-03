package com.lj.rpc.loadBalance;

import com.lj.rpc.common.extension.SPI;
import com.lj.rpc.common.url.URL;
import com.lj.rpc.domain.RpcRequest;

import java.util.List;

/**
 * 负载均衡
 */
@SPI("round-robin")
public interface LoadBalance {
    /**
     * 选择服务器
     * @param candidates 候选的URL
     * @param request 请求
     * @return 选择的URL
     */
    URL select(List<URL> candidates, RpcRequest request);
}

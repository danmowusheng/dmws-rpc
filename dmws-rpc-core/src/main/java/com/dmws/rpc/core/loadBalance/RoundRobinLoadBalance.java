package com.dmws.rpc.core.loadBalance;

import com.dmws.rpc.common.url.URL;
import com.dmws.rpc.core.domain.RpcRequest;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-03 16:47
 * @description： 轮询负载均衡
 **/
public class RoundRobinLoadBalance extends AbstractLoadBalance{

    private final LongAdder curIndex = new LongAdder();

    @Override
    protected URL doSelect(List<URL> candidates, RpcRequest request) {
        int index = (int)(curIndex.longValue() % candidates.size());
        curIndex.increment();
        return candidates.get(index);
    }
}

package com.lj.rpc.loadBalance;

import cn.hutool.core.util.RandomUtil;
import com.lj.rpc.common.url.URL;
import com.lj.rpc.domain.RpcRequest;

import java.util.List;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-03 16:40
 * @description： 随机负载均衡
 **/
public class RandomLoadBalance extends AbstractLoadBalance{
    @Override
    protected URL doSelect(List<URL> candidates, RpcRequest request) {
        int size = candidates.size();
        int index = RandomUtil.randomInt(size);
        return candidates.get(index);
    }
}

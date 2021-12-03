package com.lj.rpc.loadBalance;

import cn.hutool.core.collection.CollectionUtil;
import com.lj.rpc.common.url.URL;
import com.lj.rpc.domain.RpcRequest;

import java.util.List;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-03 16:36
 * @description： 抽象负载均衡
 **/
public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public URL select(List<URL> candidates, RpcRequest request) {
        if (CollectionUtil.isEmpty(candidates)){
            return null;
        }

        if (candidates.size() == 1){
            return candidates.get(0);
        }
        return doSelect(candidates, request);
    }

    /**
     * 真正做出选择的方法，保护不被外界使用
     * @param candidates
     * @param request
     * @return
     */
    protected abstract URL doSelect(List<URL> candidates, RpcRequest request);
}

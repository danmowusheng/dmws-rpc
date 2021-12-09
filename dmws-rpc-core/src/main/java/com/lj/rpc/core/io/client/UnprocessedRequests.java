package com.lj.rpc.core.io.client;

import cn.hutool.json.JSONUtil;
import com.lj.rpc.core.domain.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-26 16:49
 * @description： 未处理请求的封装
 **/
public class UnprocessedRequests {
    private static final Map<Long, CompletableFuture<RpcResponse<?>>> FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 向Map中添加新的未处理请求
     * 通过使用这样一层封装，使得外界无法访问Map修改Map的信息
     * @param requestId
     * @param future
     */
    public static void put(Long requestId, CompletableFuture<RpcResponse<?>> future){
        FUTURE_MAP.put(requestId, future);
    }

    public static void complete(RpcResponse<?> rpcResponse){
        CompletableFuture<RpcResponse<?>> future = FUTURE_MAP.remove(rpcResponse.getRequestId());
        if(future != null){
            future.complete(rpcResponse);
        }else {
            throw new IllegalStateException("future is null. response="+ JSONUtil.toJsonStr(rpcResponse));
        }
    }
}

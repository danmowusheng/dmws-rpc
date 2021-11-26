package com.lj.rpc.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-26 15:52
 * @description： 异步返回结构封装
 **/
@Slf4j
public class AsyncResult implements RpcResult{

    private final CompletableFuture<?> future;

    public AsyncResult(CompletableFuture<?> future) {
        this.future = future;
    }

    @Override
    public boolean isSuccess() {
        return !future.isCompletedExceptionally();
    }

    @Override
    public Object getData() {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("get data error:", e);
        }
        return null;
    }
}

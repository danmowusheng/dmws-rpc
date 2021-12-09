package com.lj.rpc.core.invocation;

import com.lj.rpc.common.extension.SPI;
import com.lj.rpc.core.domain.RpcRequest;
import com.lj.rpc.core.domain.RpcResult;
import com.lj.rpc.core.exception.RpcException;

/**
 * 执行者
 */
@SPI("netty")
public interface Invoker {
    /**
     * 执行函数
     * @param request 请求
     * @return result
     * @throws RpcException 执行时产生的异常
     */
    RpcResult invoke(RpcRequest request) throws RpcException;
}

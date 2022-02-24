package com.dmws.rpc.core.invocation;

import com.dmws.rpc.common.extension.SPI;
import com.dmws.rpc.core.domain.RpcRequest;
import com.dmws.rpc.core.domain.RpcResult;
import com.dmws.rpc.core.exception.RpcException;

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

package com.dmws.rpc.core.faulttolerant;

import com.dmws.rpc.core.domain.RpcRequest;
import com.dmws.rpc.core.domain.RpcResult;
import com.dmws.rpc.core.exception.RpcException;
import com.dmws.rpc.core.invocation.Invoker;
import com.dmws.rpc.core.loadBalance.LoadBalance;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-04 11:55
 * @description： 快速失败
 **/
public class FailFastInvoker extends AbstractFaultTolerantInvoker{
    @Override
    protected RpcResult doInvoke(RpcRequest request, Invoker invoker, LoadBalance loadBalance) throws RpcException {
        return invoker.invoke(request);
    }
}

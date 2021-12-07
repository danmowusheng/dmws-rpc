package com.lj.rpc.faulttolerant;

import com.lj.rpc.domain.RpcRequest;
import com.lj.rpc.domain.RpcResult;
import com.lj.rpc.exception.RpcException;
import com.lj.rpc.invocation.Invoker;
import com.lj.rpc.loadBalance.LoadBalance;

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

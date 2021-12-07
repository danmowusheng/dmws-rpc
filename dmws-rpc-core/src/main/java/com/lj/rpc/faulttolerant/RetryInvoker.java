package com.lj.rpc.faulttolerant;

import com.lj.rpc.domain.RpcRequest;
import com.lj.rpc.domain.RpcResult;
import com.lj.rpc.exception.RpcException;
import com.lj.rpc.invocation.Invoker;
import com.lj.rpc.loadBalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-04 11:56
 * @description： 失败后重试
 **/
@Slf4j
public class RetryInvoker extends AbstractFaultTolerantInvoker{

    //TODO:这里应该还能自己设置重试次数才对

    /**
     * 默认重试次数
     */
    private static final Integer DEFAULT_RETRY_TIMES = 3;

    @Override
    protected RpcResult doInvoke(RpcRequest request, Invoker invoker, LoadBalance loadBalance) throws RpcException {
        int retryTimes = Optional.ofNullable(clusterConfig.getRetryTimes()).orElse(DEFAULT_RETRY_TIMES);
        RpcException rpcException = null;
        for(int i=0;i<retryTimes;i++){
            try {
                RpcResult result = invoker.invoke(request);
                if (result.isSuccess()){
                    return result;
                }
            }catch (RpcException ex){
                log.error("invoke error. retry times="+i, ex);
                rpcException = ex;
            }
        }

        if (rpcException != null){
            rpcException = new RpcException("invoker error. request=" + request);
        }
        //重试失败后抛出异常
        throw rpcException;
    }
}

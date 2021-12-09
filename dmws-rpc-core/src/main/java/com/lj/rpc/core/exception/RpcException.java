package com.lj.rpc.core.exception;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-26 16:26
 * @description：RPC异常
 **/
public class RpcException extends Exception{
    public RpcException(String message){
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}

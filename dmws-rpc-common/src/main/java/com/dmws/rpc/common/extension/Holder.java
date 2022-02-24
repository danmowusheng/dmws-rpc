package com.dmws.rpc.common.extension;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-24 19:01
 * @description： 持有者, 用于单个变量既可以做锁又可以做值
 **/
public class Holder<T> {
    private T value;

    public T get(){
        return value;
    }

    public void set(T value){
        this.value = value;
    }
}

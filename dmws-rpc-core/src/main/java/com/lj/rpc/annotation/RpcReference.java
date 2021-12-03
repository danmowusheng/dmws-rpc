package com.lj.rpc.annotation;

import java.lang.annotation.*;

/**
 * RPC引用，用于标注服务调用方
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    /**
     * 版本，没有特殊要求不用填写
     * @return 版本
     */
    String version() default "";
}

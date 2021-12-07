package com.lj.rpc.annotation;

import java.lang.annotation.*;

/**
 * 放在配置类上，可以使用 ConfigLoader 加载
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    /**
     * 前缀名
     * @return 前缀，不能为空
     */
    String prefix();
}

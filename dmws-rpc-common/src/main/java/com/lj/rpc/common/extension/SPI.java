package com.lj.rpc.common.extension;

import com.lj.rpc.common.constants.URLKeyConstants;

import java.lang.annotation.*;

/**
 * 被此注解标记的类，表示是一个扩展接口
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SPI {
    /**
     * 默认扩展全路径
     * @return 默认不填为 default
     */
    String value() default URLKeyConstants.DEFAULT;
}

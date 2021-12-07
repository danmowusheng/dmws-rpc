package com.lj.rpc.annotation;

import com.lj.rpc.spring.RpcScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * RPC服务扫描?
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcScannerRegistrar.class)
public @interface RpcScan {
    String[] basePackages();
}

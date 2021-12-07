package com.lj.rpc.faulttolerant;

import com.lj.rpc.common.extension.SPI;
import com.lj.rpc.invocation.Invoker;

/**
 * 容错
 */
@SPI("fail-fast")
public interface FaultTolerantInvoker extends Invoker {
}

package com.lj.rpc.core.faulttolerant;

import com.lj.rpc.common.extension.SPI;
import com.lj.rpc.core.invocation.Invoker;

/**
 * 容错
 */
@SPI("fail-fast")
public interface FaultTolerantInvoker extends Invoker {
}

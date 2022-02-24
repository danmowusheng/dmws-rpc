package com.dmws.rpc.core.faulttolerant;

import com.dmws.rpc.common.extension.SPI;
import com.dmws.rpc.core.invocation.Invoker;

/**
 * 容错
 */
@SPI("fail-fast")
public interface FaultTolerantInvoker extends Invoker {
}

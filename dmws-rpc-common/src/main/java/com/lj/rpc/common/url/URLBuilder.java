package com.lj.rpc.common.url;

import cn.hutool.core.map.MapUtil;
import com.lj.rpc.common.constants.URLKeyConstants;

import java.util.Map;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-24 20:16
 * @description：
 **/
public class URLBuilder {
    /**
     * 获取url上相关服务的参数
     *
     * @return 参数
     */
    public static Map<String, String> getServiceParam(String interfaceName, String rpcVersion) {
        return MapUtil.<String, String>builder()
                .put(URLKeyConstants.INTERFACE, interfaceName)
                .put(URLKeyConstants.VERSION, rpcVersion).build();
    }

    /**
     * 获取url上相关服务的参数
     *
     * @return 参数
     */
    public static Map<String, String> getServiceParam(Class<?> interfaceClass, String rpcVersion) {
        return getServiceParam(interfaceClass.getCanonicalName(), rpcVersion);
    }
}

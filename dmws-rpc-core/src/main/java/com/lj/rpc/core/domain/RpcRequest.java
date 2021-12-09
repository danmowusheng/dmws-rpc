package com.lj.rpc.core.domain;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-26 15:13
 * @description： 请求实体
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest {
    /**
     * 接口名
     */
    private String interfaceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数列表
     */
    private Object[] params;
    /**
     * 参数类型列表
     */
    private Class<?>[] paramTypes;
    /**
     * 接口版本
     */
    private String version;

    /**
    * TODO: 这个方法是做什么的，后续要搞清楚
    *
    * @author: LJ
    * @Date: 2021/11/26
    **/
    public String getRpcServiceForCache(){
        if(StrUtil.isBlank(version)){
            return interfaceName;
        }
        return interfaceName + "_" + version;
    }
}

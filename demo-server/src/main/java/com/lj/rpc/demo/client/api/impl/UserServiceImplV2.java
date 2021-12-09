package com.lj.rpc.demo.client.api.impl;

import com.lj.rpc.demo.client.api.UserService;
import com.lj.rpc.demo.client.bean.UserInfo;
import com.lj.rpc.core.annotation.RpcService;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 15:09
 * @description： 用户服务版本2
 **/
@RpcService
public class UserServiceImplV2 implements UserService {
    @Override
    public UserInfo getUser(Long id) {
        return UserInfo.builder().userId(id).userName("v2-user"+id).build();
    }
}

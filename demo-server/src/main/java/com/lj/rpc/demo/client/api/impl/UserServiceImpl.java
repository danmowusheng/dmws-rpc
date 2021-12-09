package com.lj.rpc.demo.client.api.impl;

import com.lj.rpc.demo.client.api.UserService;
import com.lj.rpc.demo.client.bean.UserInfo;
import com.lj.rpc.core.annotation.RpcService;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 15:07
 * @description： 用户服务
 **/
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public UserInfo getUser(Long id) {
        return UserInfo.builder().userId(id).userName("user"+id).build();
    }
}

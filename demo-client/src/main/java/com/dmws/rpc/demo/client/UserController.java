package com.dmws.rpc.demo.client;


import com.dmws.rpc.demo.server.api.UserService;
import com.dmws.rpc.demo.server.bean.UserInfo;
import com.dmws.rpc.core.annotation.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-07 21:04
 * @description： 用户控制器
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @RpcReference
    private UserService userService;

    @RpcReference
    private UserService userServiceV2;

    @GetMapping("/{uid}")
    public UserInfo getUser(@PathVariable("uid") long uid){
        return userService.getUser(uid);
    }

    @GetMapping("/v2/{uid}")
    public UserInfo getUserV2(@PathVariable("uid") long uid){
        return userServiceV2.getUser(uid);
    }
}
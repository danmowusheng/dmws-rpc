package com.lj.rpc.demo.client.bean;

import lombok.*;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-12-09 15:04
 * @description： 用户信息
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private long userId;
    private String userName;
}

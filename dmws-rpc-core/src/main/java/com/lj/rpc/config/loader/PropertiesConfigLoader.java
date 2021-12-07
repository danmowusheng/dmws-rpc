package com.lj.rpc.config.loader;


import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: dmws-rpc
 * @author: LJ
 * @create: 2021-11-28 15:31
 * @description： dmws-rpc.properties 文件配置。prefix.configField=xxx
 **/
@Slf4j
public class PropertiesConfigLoader implements ConfigLoader {

    private Setting setting = null;

    public PropertiesConfigLoader(){
        try {
            setting = SettingUtil.get("dmws-rpc.properties");
        }catch (NoResourceException e){
            log.warn("Config file 'dmws-rpc.properties' not exist!");
        }
    }


    @Override
    public String loadConfigItem(String key) {
        if(setting == null)return null;

        return setting.getStr(key);
    }
}

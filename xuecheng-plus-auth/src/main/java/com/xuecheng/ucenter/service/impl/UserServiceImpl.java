package com.xuecheng.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.ucenter.mapper.XcMenuMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcMenu;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/2/24 10:37
 */
@Slf4j
@Component
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    XcUserMapper xcUserMapper;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    XcMenuMapper xcMenuMapper;


    //传入的请求认证的参数就是AuthParamsDto
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthParamsDto authParamsDto = null;
        try {
            authParamsDto = JSON.parseObject(s, AuthParamsDto.class);
        } catch (Exception e) {
            log.error("认证请求数据格式不对：{}", s);
            throw new RuntimeException("认证请求数据格式不对");
        }
        // 获取认证类型，beanName就是 认证类型 + 后缀，例如 password + _authservice = password_authservice
        String authType = authParamsDto.getAuthType();
        // 根据认证类型，从Spring容器中取出对应的bean
        AuthService authService = applicationContext.getBean(authType + "_authservice", AuthService.class);
        XcUserExt user = authService.execute(authParamsDto);

        return getUserPrincipal(user);
    }

    /**
     * @param user 用户id，主键
     * @return com.xuecheng.ucenter.model.po.XcUser 用户信息
     * @description 查询用户信息
     * @author Mr.M
     * @date 2022/9/29 12:19
     */
    public UserDetails getUserPrincipal(XcUserExt user) {
        // 获取用户id
        String userId = user.getId();
        // 根据用户id查询用户权限
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
        ArrayList<String> permissions = new ArrayList<>();
        // 没权限，给一个默认的
        if (xcMenus.isEmpty()) {
            permissions.add("test");
        } else {
            // 获取权限，加入到集合里
            xcMenus.forEach(xcMenu -> {
                permissions.add(xcMenu.getCode());
            });
        }// 设置权限
        user.setPermissions(permissions);
        String[] authorities = permissions.toArray(new String[0]);
        String password = user.getPassword();
        user.setPassword(null);
        String userJsonStr = JSON.toJSONString(user);
        UserDetails userDetails = User.withUsername(userJsonStr).password(password).authorities(authorities).build();
        return userDetails;
    }


}

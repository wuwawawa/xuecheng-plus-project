package com.xuecheng.checkcode.service;

public interface SendCodeService {

    /**
     * 向目标邮箱发送验证码
     *
     * @param email 目标邮箱
     */
    void sendEMail(String email);

}

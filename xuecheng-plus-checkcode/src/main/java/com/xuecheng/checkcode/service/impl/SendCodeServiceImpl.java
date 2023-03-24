package com.xuecheng.checkcode.service.impl;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.checkcode.service.SendCodeService;
import com.xuecheng.checkcode.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SendCodeServiceImpl implements SendCodeService {
    public final Long CODE_TTL = 120L;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void sendEMail(String email) {
        String code = null;
        // 1. 向用户发送验证码
        try {
            code = MailUtil.sendMail(email);
        } catch (MessagingException e) {
            log.debug("邮件发送失败：{}", e.getMessage());
            XueChengPlusException.cast("发送验证码失败，请稍后再试");
        }
        // 2. 将验证码缓存到redis，TTL设置为2分钟
        redisTemplate.opsForValue().set("findPassword:" + email, code, CODE_TTL, TimeUnit.SECONDS);
    }
}
package com.grant.MiShe.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.grant.MiShe.common.constant.RedisConstant;
import com.grant.MiShe.common.exception.LeaseException;
import com.grant.MiShe.common.result.ResultCodeEnum;
import com.grant.MiShe.common.utils.JWTUtils;
import com.grant.MiShe.model.entity.SystemUser;
import com.grant.MiShe.model.enums.BaseStatus;
import com.grant.MiShe.web.admin.mapper.SystemUserMapper;
import com.grant.MiShe.web.admin.service.LoginService;
import com.grant.MiShe.web.admin.vo.login.CaptchaVo;
import com.grant.MiShe.web.admin.vo.login.LoginVo;
import com.wf.captcha.SpecCaptcha;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public CaptchaVo getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130,48,4);

        String code = specCaptcha.text().toLowerCase();
        String key= RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();

        stringRedisTemplate.opsForValue().set(key,code,RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);
        return new CaptchaVo(specCaptcha.toBase64(),key);
    }

    @Override
    public String login(LoginVo loginVo) {
        if(loginVo.getCaptchaCode()==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }

        if(loginVo.getCaptchaKey()==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }
        String s = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());

        if(!s.equals(loginVo.getCaptchaCode().toLowerCase())){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }


        SystemUser systemUser = systemUserMapper.selectOneByUserName(loginVo.getUsername());


        if(systemUser==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }

        if(systemUser.getStatus()== BaseStatus.DISABLE){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }

        if(!systemUser.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }


        return JWTUtils.creatJWT(systemUser.getId(),systemUser.getUsername());
    }
}

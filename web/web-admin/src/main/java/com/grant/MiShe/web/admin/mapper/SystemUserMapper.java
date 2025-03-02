package com.grant.MiShe.web.admin.mapper;

import com.grant.MiShe.model.entity.SystemUser;
import com.grant.MiShe.web.admin.vo.system.user.SystemUserItemVo;
import com.grant.MiShe.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
* @author liubo
* @description 针对表【system_user(员工信息表)】的数据库操作Mapper
* @createDate 2023-07-24 15:48:00
* @Entity com.grant.MiShe.model.SystemUser
*/
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    IPage<SystemUserItemVo> pageSystemUserByQuery(IPage<SystemUser> page, SystemUserQueryVo queryVo);

    SystemUser selectOneByUserName(String username);
}





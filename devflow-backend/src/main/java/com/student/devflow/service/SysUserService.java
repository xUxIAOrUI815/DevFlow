package com.student.devflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.devflow.entity.SysUser;

public interface SysUserService extends IService<SysUser>{
    // 登录校验、重置密码

    // 注册
    void register(SysUser user);
}


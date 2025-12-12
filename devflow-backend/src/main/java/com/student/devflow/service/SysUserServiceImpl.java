package com.student.devflow.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.devflow.entity.SysUser;
import com.student.devflow.mapper.SysUserMapper;
import com.student.devflow.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}

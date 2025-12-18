package com.student.devflow.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.devflow.entity.SysUser;
import com.student.devflow.mapper.SysUserMapper;
import com.student.devflow.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public void register(SysUser user){
        // 校验非空
        if(user.getUsername() == null || user.getPassword () == null){
            throw new RuntimeException("用户名和密码不能为空");
        }

        // 检查用户名是否已经存在
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("username",user.getUsername());
        if(this.count(query)>0){
            throw new RuntimeException("用户名已存在，请换一个");
        }

        // 设置默认值
        if(user.getRealName() == null){
            user.setRealName(user.getUsername());   // 默认使用用户名
        }
        user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");

        // 真实项目需要给密码加密
        this.save(user);
    }
}

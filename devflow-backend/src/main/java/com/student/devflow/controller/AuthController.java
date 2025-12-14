package com.student.devflow.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.devflow.common.Result;
import com.student.devflow.entity.SysUser;
import com.student.devflow.service.SysUserService;
import com.student.devflow.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody SysUser loginForm) {
        // 1. 查询用户
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("username",loginForm.getUsername());
        SysUser user = sysUserService.getOne(query);

        // 2. 校验密码
        if(user == null || !user.getPassword().equals(loginForm.getPassword())){
            // 实际项目中不应该使用明文比对，而是用 BCrypt
            return Result.error("用户名或密码错误");
        }

        // 生成 Token
        String token = JwtUtils.generateToken(user.getId(), user.getUsername());

        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return Result.success(result);
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody SysUser registerForm) {

        // 1. 验证用户名是否已存在
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("username",registerForm.getUsername());
        SysUser user = sysUserService.getOne(query);
        if(user != null){
            return Result.error("用户名已存在");
        }
        sysUserService.save(registerForm);
        return Result.success("注册成功");
    }
}

package com.student.devflow.controller;

import com.student.devflow.common.Result;
import com.student.devflow.entity.SysUser;
import com.student.devflow.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin        // 跨域
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    // 获取所有用户 用于前端下拉框选择
    @GetMapping("/list")
    public Result<List<SysUser>> list(){
        return Result.success(sysUserService.list());
    }
}

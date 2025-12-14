package com.student.devflow.controller;

import com.student.devflow.common.Result;
import com.student.devflow.entity.ProjectMember;
import com.student.devflow.entity.vo.ProjectMemberVO;
import com.student.devflow.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@CrossOrigin
public class ProjectMemberController {
    @Autowired
    private ProjectMemberService memberService;

    // 获取成员列表
    @GetMapping("/list/{projectId}")
    public Result<List<ProjectMemberVO>> list(@PathVariable Long projectId){
        return Result.success(memberService.getProjectMembers(projectId));
    }

    // 添加成员
    @PostMapping("/add")
    public Result<String> add(@RequestBody ProjectMember member){
        try {
            memberService.addMember(member.getProjectId(), member.getUserId(), member.getRole());
            return Result.success("添加成功");
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }

    // 移除成员
    @DeleteMapping("/remove/{id}")
    public Result<String> remove(@PathVariable Long id){
        memberService.removeById(id);
        return Result.success("移除成功");
    }
}

package com.student.devflow.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.student.devflow.common.Result;
import com.student.devflow.entity.Issue;
import com.student.devflow.entity.vo.IssueTreeVO;
import com.student.devflow.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/issue")
@CrossOrigin
public class IssueController {

    @Autowired
    private IssueService issueService;

    // 树形用
    @GetMapping("/tree/{projectId}")
    public Result<List<IssueTreeVO>> getTree(@PathVariable Long projectId){
        return Result.success(issueService.getProjectIssueTree(projectId));
    }

    // 看板用
    @GetMapping("/list/{projectId}")
    public Result<List<Issue>> listByProject(@PathVariable Long projectId) {
        QueryWrapper<Issue> query = new QueryWrapper<>();
        query.eq("project_id", projectId);
        // 按创建时间倒序，新任务排前面
        query.orderByDesc("created_at");
        return Result.success(issueService.list(query));
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody Issue issue){
        issueService.save(issue);
        return Result.success("Created SUCCESS! ");
    }
    @PutMapping("/update")
    public Result<String> updateStatus(@RequestBody Issue issue){
        issueService.updateIssueStatus(issue);
        return Result.success("Update Success");

    }

}

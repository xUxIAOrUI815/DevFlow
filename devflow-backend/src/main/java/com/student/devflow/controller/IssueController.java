package com.student.devflow.controller;

import com.student.devflow.common.Result;
import com.student.devflow.entity.Issue;
import com.student.devflow.entity.vo.IssueTreeVO;
import com.student.devflow.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issue")
@CrossOrigin
public class IssueController {

    @Autowired
    private IssueService issueService;

    @GetMapping("/tree/{id}")
    public Result<IssueTreeVO> getTree(@PathVariable Long id){
        return Result.success(issueService.getIssueTree(id));
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

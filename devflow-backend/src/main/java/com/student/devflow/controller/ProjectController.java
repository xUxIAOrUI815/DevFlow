package com.student.devflow.controller;

import com.student.devflow.common.Result;
import com.student.devflow.entity.Project;
import com.student.devflow.entity.vo.ProjectVO;
import com.student.devflow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/project")
@CrossOrigin        // 允许跨域
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/list")
    public Result<List<ProjectVO>> list(){
        return Result.success(projectService.getProjectList());
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody Project project){
        System.out.println("接受到新项目请求: "+project);
        projectService.save(project);
        return Result.success("Created SUCCESS! ");
    }
}

package com.student.devflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.devflow.entity.Project;
import com.student.devflow.entity.vo.ProjectVO;
import java.util.List;

public interface ProjectService extends IService<Project>{
    List<ProjectVO> getProjectList();
}

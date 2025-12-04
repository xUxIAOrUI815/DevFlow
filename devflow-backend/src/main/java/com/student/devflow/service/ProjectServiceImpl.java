package com.student.devflow.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.devflow.entity.Project;
import com.student.devflow.entity.vo.ProjectVO;
import com.student.devflow.mapper.ProjectMapper;
import com.student.devflow.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectServiceImpl
        extends ServiceImpl<ProjectMapper, Project>
        implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Override
    public List<ProjectVO> getProjectList(){
        return projectMapper.selectProjectListWithStats();
    }
}

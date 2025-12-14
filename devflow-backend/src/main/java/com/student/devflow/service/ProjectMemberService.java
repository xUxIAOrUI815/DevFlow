package com.student.devflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.devflow.entity.ProjectMember;
import com.student.devflow.entity.vo.ProjectMemberVO;
import java.util.List;

public interface ProjectMemberService extends IService<ProjectMember>{
    List<ProjectMemberVO> getProjectMembers(Long projectId);

    // 添加成员
    boolean addMember(Long projectId, Long userId, Integer role);

}

package com.student.devflow.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.devflow.entity.ProjectMember;
import com.student.devflow.entity.vo.ProjectMemberVO;
import com.student.devflow.mapper.ProjectMemberMapper;
import com.student.devflow.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectMemberServiceImpl extends ServiceImpl<ProjectMemberMapper, ProjectMember> implements ProjectMemberService {    @Autowired
    private ProjectMemberMapper memberMapper;

    @Override
    public List<ProjectMemberVO> getProjectMembers(Long projectId) {
        return memberMapper.getProjectMembers(projectId);
    }

    @Override
    public boolean addMember(Long projectId, Long userId, Integer role){

        // 检查是否已经存在
        QueryWrapper<ProjectMember> query = new QueryWrapper<>();
        query.eq("project_id", projectId).eq("user_id", userId);
        if(this.count(query) > 0){
            // 说明已经存在
            throw new RuntimeException("该用户已经是项目成员，请勿重复添加");
        }

        // 不存在 则可以添加
        ProjectMember member = new ProjectMember();
        member.setProjectId(projectId);
        member.setUserId(userId);
        member.setRole(role);
        return this.save(member);
    }
}

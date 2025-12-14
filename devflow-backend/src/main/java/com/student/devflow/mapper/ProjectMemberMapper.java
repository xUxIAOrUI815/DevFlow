package com.student.devflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.devflow.entity.ProjectMember;
import com.student.devflow.entity.vo.ProjectMemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {

    // 关联 sys_user 表 查询某项目的所有成员详情
    @Select("SELECT " +
            "pm.id, pm.user_id, pm.role, pm.joined_at" +
            "u.real_name, u.avatar" +
            "FROM project_member pm" +
            "LEFT JOIN sys_user u ON pm.user_id = u.id" +
            "WHERE pm.project_id = #{projectId}" +
            "ORDER BY pm.role DESC, pm.joined_at ASC")
    List<ProjectMemberVO> getProjectMembers(@Param("projectId") Long projectId);

}

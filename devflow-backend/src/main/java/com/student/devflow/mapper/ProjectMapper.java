package com.student.devflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.devflow.entity.Project;
import com.student.devflow.entity.vo.ProjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    @Select
            ("SELECT p.id, p.name, p.code, p.description, p.created_at, " +
            "u.real_name AS ownerName, " +
            "COUNT(i.id) AS taskCount, " +
            "SUM(CASE WHEN i.status = 2 THEN 1 ELSE 0 END) AS doneCount " +
            "FROM project p " +
            "LEFT JOIN sys_user u ON p.owner_id = u.id " +
            "LEFT JOIN issue i ON p.id = i.project_id " +
            "GROUP BY p.id, p.name, p.code, p.description, p.created_at, u.real_name " +
            "ORDER BY p.id DESC")

    // 自定义查询: 获取项目列表以及进度统计
    List<ProjectVO> selectProjectListWithStats();
}
package com.student.devflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.devflow.entity.Issue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface IssueMapper extends BaseMapper<Issue>{
    @Select("WITH RECURSIVE task_tree AS (" +
            " /* 1. 锚点: 查找指定的根任务 */ " +
            " SELECT * FROM issue WHERE id = #{rootId} " +
            "UNION ALL " +
            " /* 2. 递归: 查找子任务 (当前表的id = 子表的parent_id */" +
            " SELECT child.* FROM issue child " +
            " INNER JOIN task_tree parent ON child.parent_id = parent.id" +
            ")" +
            " SELECT * FROM task_tree")
    List<Issue> selectIssueTree(Long rootId);
}

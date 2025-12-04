package com.student.devflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务实体类
 */
@Data
@TableName("issue")
public class Issue {
    @TableId(type=IdType.AUTO)
    private Long id;
    private Long projectId;
    private String title;
    private String description;
    private Integer type;
    private Integer status;
    private Integer priority;
    private Long parentId;
    private Long assigneeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

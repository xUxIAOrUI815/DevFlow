package com.student.devflow.entity.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectVO {
    private Long id;
    private String name;
    private String code;
    private String description;

    // 专门用于前端展示的数据
    private String ownerName;   // 项目负责人名字 从sys_user表中查找
    private Integer taskCount;  // 任务总数 聚合计算
    private Integer doneCount;  // 已完成数 聚合计算

    private LocalDateTime createdAt;
}

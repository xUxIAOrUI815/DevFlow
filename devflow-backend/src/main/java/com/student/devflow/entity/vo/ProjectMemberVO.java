package com.student.devflow.entity.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectMemberVO {
    private Long id;    // 成员记录id
    private Long userId;
    private String realName;    // 从 sys_user 中获取
    private String avatar;      // 从 sys_user 中获取
    private Integer role;
    private LocalDateTime joinedAt;
}

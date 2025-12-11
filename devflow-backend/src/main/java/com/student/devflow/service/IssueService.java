package com.student.devflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.student.devflow.entity.Issue;
import com.student.devflow.entity.vo.IssueTreeVO;

public interface IssueService extends IService<Issue>{
    IssueTreeVO getIssueTree(Long rootId);

    void updateIssueStatus(Issue issue);
}

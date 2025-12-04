package com.student.devflow.entity.vo;

import com.student.devflow.entity.Issue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class IssueTreeVO extends Issue{

    private List<IssueTreeVO> children;
}

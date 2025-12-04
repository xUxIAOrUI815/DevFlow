package com.student.devflow.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.devflow.entity.Issue;
import com.student.devflow.entity.vo.IssueTreeVO;
import com.student.devflow.mapper.IssueMapper;
import com.student.devflow.service.IssueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService{

    @Autowired
    private IssueMapper issueMapper;

    @Override
    public IssueTreeVO getIssueTree(Long rootId){
        // 1. 先用递归 SQL 查出所有相关的扁平数据
        List<Issue> flatList = issueMapper.selectIssueTree(rootId);

        if(flatList.isEmpty()){
            return null;
        }

        // 2. 将 Issue 转换成 IssueTreeVO
        List<IssueTreeVO> voList = new ArrayList<>();
        for(Issue issue : flatList){
            IssueTreeVO vo = new IssueTreeVO();
            BeanUtils.copyProperties(issue, vo);        // 复制属性
            voList.add(vo);
        }

        // 3. 利用 Map 将扁平 List 组装成 Tree
        // 因为返回结果是二维表格，没有体现包含关系
        Map<Long, IssueTreeVO> map = voList.stream()
            .collect(Collectors.toMap(IssueTreeVO::getId, Function.identity()));

        IssueTreeVO root = null;

        for(IssueTreeVO node : voList){
            if(node.getId().equals(rootId)){
                root = node;
            }else{
                IssueTreeVO parent = map.get(node.getParentId());
                if(parent != null){
                    if(parent.getChildren() == null){
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(node);
                }
            }
        }

        return root;
    }
}

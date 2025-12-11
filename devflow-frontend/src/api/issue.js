import request from '@/utils/request'

// 获取任务树 递归结构
export function getIssueTree(rootId) {
  return request({
    url: `/issue/tree/${rootId}`,
    method: 'get'
  })
}

// 快速创建任务
export function addIssue(data){
  return request({
    url: '/issue/add',
    method: 'post',
    data
  })
}

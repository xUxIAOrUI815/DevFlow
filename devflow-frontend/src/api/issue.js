import request from '@/utils/request'

// 1. 获取任务树 (递归结构) - 树形表格用
export function getIssueTree(rootId) {
  return request({
    url: `/issue/tree/${rootId}`,
    method: 'get'
  })
}

// 2. 创建新任务 - 弹窗用
export function addIssue(data) {
  return request({
    url: '/issue/add',
    method: 'post',
    data
  })
}

// 3. 获取项目下所有任务 - 看板用
export function getIssueList(projectId) {
  return request({
    url: `/issue/list/${projectId}`,
    method: 'get'
  })
}

// 4. 更新任务状态 - 拖拽用
export function updateIssue(data) {
  return request({
    url: '/issue/update',
    method: 'put',
    data
  })
}

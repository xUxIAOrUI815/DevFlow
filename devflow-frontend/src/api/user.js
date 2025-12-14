import request from '@/utils/request.js'

export function getUserList(){
  return request({
    url:`/user/list`,
    method:'get'
  })
}

// 获取项目成员
export function getProjectMembers(projectId){
  return request({
    url: `/member/list/${projectId}`,
    method: 'get'
  })
}

// 添加成员
export function addMember(data){
  return request({
    url: '/member/add',
    method: 'post',
    data
  })
}

// 移除成员
export function removeMember(id){
  return request({
    url: `/member/remove/${id}`,
    method: 'delete'
  })
}

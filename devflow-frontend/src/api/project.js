import request from '@/utils/request.js'

// 获取项目列表
export function getProjectList(){
    return request({
        url:'/project/list',
        method:'get'
    })
}

// 创建项目
export function addProject(data){
  return request({
    url:'/project/add',
    method:'post',
    data
  })
}

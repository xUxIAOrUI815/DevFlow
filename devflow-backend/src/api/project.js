import request from '@/utils/request'

// 获取项目列表
export function getProjectList(){
    return request({
        url:'/project/list',
        method:'get'
    })
}
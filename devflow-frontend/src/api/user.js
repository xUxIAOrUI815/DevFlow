import request from '@/utils/request.js'

export function getUserList(){
  return request({
    url:'/user/list',
    method:'get'
  })
}

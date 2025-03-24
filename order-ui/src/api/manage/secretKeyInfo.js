import request from '@/utils/request'

// 查询密钥信息列表
export function listSecretKeyInfo(query) {
  return request({
    url: '/manage/secretKeyInfo/list',
    method: 'get',
    params: query
  })
}

// 查询密钥信息详细
export function getSecretKeyInfo(id) {
  return request({
    url: '/manage/secretKeyInfo/' + id,
    method: 'get'
  })
}

// 新增密钥信息
export function addSecretKeyInfo(data) {
  return request({
    url: '/manage/secretKeyInfo',
    method: 'post',
    data: data
  })
}

// 修改密钥信息
export function updateSecretKeyInfo(data) {
  return request({
    url: '/manage/secretKeyInfo',
    method: 'put',
    data: data
  })
}

// 删除密钥信息
export function delSecretKeyInfo(id) {
  return request({
    url: '/manage/secretKeyInfo/' + id,
    method: 'delete'
  })
}

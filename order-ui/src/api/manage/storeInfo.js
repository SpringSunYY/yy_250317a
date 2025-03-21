import request from '@/utils/request'

// 查询店铺信息列表
export function listStoreInfo(query) {
  return request({
    url: '/manage/storeInfo/list',
    method: 'get',
    params: query
  })
}

// 查询店铺信息详细
export function getStoreInfo(id) {
  return request({
    url: '/manage/storeInfo/' + id,
    method: 'get'
  })
}

// 新增店铺信息
export function addStoreInfo(data) {
  return request({
    url: '/manage/storeInfo',
    method: 'post',
    data: data
  })
}

// 修改店铺信息
export function updateStoreInfo(data) {
  return request({
    url: '/manage/storeInfo',
    method: 'put',
    data: data
  })
}

// 删除店铺信息
export function delStoreInfo(id) {
  return request({
    url: '/manage/storeInfo/' + id,
    method: 'delete'
  })
}

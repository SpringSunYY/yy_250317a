import request from '@/utils/request'

// 查询订单列表
export function listOrderInfo(query) {
  return request({
    url: '/manage/orderInfo/list',
    method: 'get',
    params: query
  })
}

export function getOrderInfoByApi(query) {
  return request({
    url: '/manage/orderInfo/getOrderInfoByApi',
    method: 'get',
    params: query,
    timeout: 3000000
  })
}

// 查询订单详细
export function getOrderInfo(id) {
  return request({
    url: '/manage/orderInfo/' + id,
    method: 'get'
  })
}

// 新增订单
export function addOrderInfo(data) {
  return request({
    url: '/manage/orderInfo',
    method: 'post',
    data: data
  })
}

// 修改订单
export function updateOrderInfo(data) {
  return request({
    url: '/manage/orderInfo',
    method: 'put',
    data: data
  })
}

// 删除订单
export function delOrderInfo(id) {
  return request({
    url: '/manage/orderInfo/' + id,
    method: 'delete'
  })
}

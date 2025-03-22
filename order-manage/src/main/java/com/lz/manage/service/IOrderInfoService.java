package com.lz.manage.service;

import java.util.List;

import com.lz.manage.model.api.OrderInfoResponse;
import com.lz.manage.model.domain.OrderInfo;
import com.lz.manage.model.dto.orderInfo.OrderInfoApiQuery;
import com.lz.manage.model.vo.orderInfo.OrderInfoVo;
import com.lz.manage.model.dto.orderInfo.OrderInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 订单Service接口
 *
 * @author ruoyi
 * @date 2025-03-21
 */
public interface IOrderInfoService extends IService<OrderInfo> {
    //region mybatis代码

    /**
     * 查询订单
     *
     * @param id 订单主键
     * @return 订单
     */
    public OrderInfo selectOrderInfoById(Long id);

    /**
     * 查询订单列表
     *
     * @param orderInfo 订单
     * @return 订单集合
     */
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo);

    /**
     * 新增订单
     *
     * @param orderInfo 订单
     * @return 结果
     */
    public int insertOrderInfo(OrderInfo orderInfo);

    /**
     * 修改订单
     *
     * @param orderInfo 订单
     * @return 结果
     */
    public int updateOrderInfo(OrderInfo orderInfo);

    /**
     * 批量删除订单
     *
     * @param ids 需要删除的订单主键集合
     * @return 结果
     */
    public int deleteOrderInfoByIds(Long[] ids);

    /**
     * 删除订单信息
     *
     * @param id 订单主键
     * @return 结果
     */
    public int deleteOrderInfoById(Long id);
    //endregion

    /**
     * 获取查询条件
     *
     * @param orderInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<OrderInfo> getQueryWrapper(OrderInfoQuery orderInfoQuery);

    /**
     * 转换vo
     *
     * @param orderInfoList OrderInfo集合
     * @return OrderInfoVO集合
     */
    List<OrderInfoVo> convertVoList(List<OrderInfo> orderInfoList);

    /**
     * description: 根据api获取订单信息
     * author: YY
     * method: getOrderInfoByApi
     * date: 2025/3/22 15:37
     * param:
     * param: orderInfoApiQuery
     * return: com.lz.manage.model.api.OrderInfoResponse
     **/
    OrderInfoResponse.Data getOrderInfoByApi(OrderInfoApiQuery orderInfoApiQuery);
}

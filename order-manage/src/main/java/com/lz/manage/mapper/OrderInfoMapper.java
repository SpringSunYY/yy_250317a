package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 订单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-03-21
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo>
{
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
     * 删除订单
     * 
     * @param id 订单主键
     * @return 结果
     */
    public int deleteOrderInfoById(Long id);

    /**
     * 批量删除订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderInfoByIds(Long[] ids);
}

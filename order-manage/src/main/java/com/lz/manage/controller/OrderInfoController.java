package com.lz.manage.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.manage.model.dto.orderInfo.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.manage.model.domain.OrderInfo;
import com.lz.manage.model.vo.orderInfo.OrderInfoVo;
import com.lz.manage.service.IOrderInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 订单Controller
 *
 * @author ruoyi
 * @date 2025-03-21
 */
@RestController
@RequestMapping("/manage/orderInfo")
public class OrderInfoController extends BaseController {
    @Resource
    private IOrderInfoService orderInfoService;

    /**
     * 查询订单列表
     */
    @PreAuthorize("@ss.hasPermi('manage:orderInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderInfoQuery orderInfoQuery) {
        OrderInfo orderInfo = OrderInfoQuery.queryToObj(orderInfoQuery);
        startPage();
        List<OrderInfo> list = orderInfoService.selectOrderInfoList(orderInfo);
        List<OrderInfoVo> listVo = list.stream().map(OrderInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    @PreAuthorize("@ss.hasPermi('manage:orderInfo:add')")
    @GetMapping("/getOrderInfoByApi")
    public AjaxResult getOrderInfoByApi(@Validated OrderInfoApiQuery orderInfoApiQuery) {
        return success(orderInfoService.getOrderInfoByApi(orderInfoApiQuery));
    }

    /**
     * 导出订单列表
     */
    @PreAuthorize("@ss.hasPermi('manage:orderInfo:export')")
    @Log(title = "订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderInfoQuery orderInfoQuery) {
        OrderInfo orderInfo = OrderInfoQuery.queryToObj(orderInfoQuery);
        List<OrderInfo> list = orderInfoService.selectOrderInfoList(orderInfo);
        ExcelUtil<OrderInfo> util = new ExcelUtil<OrderInfo>(OrderInfo.class);
        util.exportExcel(response, list, "订单数据");
    }

    /**
     * 获取订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:orderInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        OrderInfo orderInfo = orderInfoService.selectOrderInfoById(id);
        return success(OrderInfoVo.objToVo(orderInfo));
    }

    /**
     * 新增订单
     */
    @PreAuthorize("@ss.hasPermi('manage:orderInfo:add')")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderInfoInsert orderInfoInsert) {
        OrderInfo orderInfo = OrderInfoInsert.insertToObj(orderInfoInsert);
        return toAjax(orderInfoService.insertOrderInfo(orderInfo));
    }

    /**
     * 外部接口新增订单
     */
    @PreAuthorize("@sk.hasKey()")
    @PostMapping("/external/add")
    public AjaxResult externalAdd(@RequestBody @Validated OrderInfoAdd orderInfoAdd) {
        OrderInfo orderInfo = OrderInfoAdd.insertToObj(orderInfoAdd);
        try {
            //拿到添加信息的时间戳再设置上去
            String evaluateTime = orderInfoAdd.getEvaluateTime();
            String scanTime = orderInfoAdd.getScanTime();
            //转换为时间
            orderInfo.setEvaluateTime(DateUtils.getTimeStr(Long.parseLong(evaluateTime)));
            orderInfo.setScanTime(DateUtils.getTimeStr(Long.parseLong(scanTime)));
        } catch (NumberFormatException e) {
            throw new ServiceException("时间搓转换异常");
        }
        return success(orderInfoService.externalAdd(orderInfo));
    }

    /**
     * 修改订单
     */
    @PreAuthorize("@ss.hasPermi('manage:orderInfo:edit')")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderInfoEdit orderInfoEdit) {
        OrderInfo orderInfo = OrderInfoEdit.editToObj(orderInfoEdit);
        return toAjax(orderInfoService.updateOrderInfo(orderInfo));
    }

    /**
     * 删除订单
     */
    @PreAuthorize("@ss.hasPermi('manage:orderInfo:remove')")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(orderInfoService.deleteOrderInfoByIds(ids));
    }
}

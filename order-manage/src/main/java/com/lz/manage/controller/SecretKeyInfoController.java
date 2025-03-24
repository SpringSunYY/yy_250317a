package com.lz.manage.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;
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
import com.lz.manage.model.domain.SecretKeyInfo;
import com.lz.manage.model.vo.secretKeyInfo.SecretKeyInfoVo;
import com.lz.manage.model.dto.secretKeyInfo.SecretKeyInfoQuery;
import com.lz.manage.model.dto.secretKeyInfo.SecretKeyInfoInsert;
import com.lz.manage.model.dto.secretKeyInfo.SecretKeyInfoEdit;
import com.lz.manage.service.ISecretKeyInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 密钥信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/manage/secretKeyInfo")
public class SecretKeyInfoController extends BaseController
{
    @Resource
    private ISecretKeyInfoService secretKeyInfoService;

    /**
     * 查询密钥信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:secretKeyInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SecretKeyInfoQuery secretKeyInfoQuery)
    {
        SecretKeyInfo secretKeyInfo = SecretKeyInfoQuery.queryToObj(secretKeyInfoQuery);
        startPage();
        List<SecretKeyInfo> list = secretKeyInfoService.selectSecretKeyInfoList(secretKeyInfo);
        List<SecretKeyInfoVo> listVo= list.stream().map(SecretKeyInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出密钥信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:secretKeyInfo:export')")
    @Log(title = "密钥信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SecretKeyInfoQuery secretKeyInfoQuery)
    {
        SecretKeyInfo secretKeyInfo = SecretKeyInfoQuery.queryToObj(secretKeyInfoQuery);
        List<SecretKeyInfo> list = secretKeyInfoService.selectSecretKeyInfoList(secretKeyInfo);
        ExcelUtil<SecretKeyInfo> util = new ExcelUtil<SecretKeyInfo>(SecretKeyInfo.class);
        util.exportExcel(response, list, "密钥信息数据");
    }

    /**
     * 获取密钥信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:secretKeyInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        SecretKeyInfo secretKeyInfo = secretKeyInfoService.selectSecretKeyInfoById(id);
        return success(SecretKeyInfoVo.objToVo(secretKeyInfo));
    }

    /**
     * 新增密钥信息
     */
    @PreAuthorize("@ss.hasPermi('manage:secretKeyInfo:add')")
    @Log(title = "密钥信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SecretKeyInfoInsert secretKeyInfoInsert)
    {
        SecretKeyInfo secretKeyInfo = SecretKeyInfoInsert.insertToObj(secretKeyInfoInsert);
        return toAjax(secretKeyInfoService.insertSecretKeyInfo(secretKeyInfo));
    }

    /**
     * 修改密钥信息
     */
    @PreAuthorize("@ss.hasPermi('manage:secretKeyInfo:edit')")
    @Log(title = "密钥信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SecretKeyInfoEdit secretKeyInfoEdit)
    {
        SecretKeyInfo secretKeyInfo = SecretKeyInfoEdit.editToObj(secretKeyInfoEdit);
        return toAjax(secretKeyInfoService.updateSecretKeyInfo(secretKeyInfo));
    }

    /**
     * 删除密钥信息
     */
    @PreAuthorize("@ss.hasPermi('manage:secretKeyInfo:remove')")
    @Log(title = "密钥信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(secretKeyInfoService.deleteSecretKeyInfoByIds(ids));
    }
}

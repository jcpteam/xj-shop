package com.javaboot.web.controller.system;

import java.util.List;

import com.javaboot.common.config.Global;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.framework.util.ShiroUtils;
import com.javaboot.system.domain.SysDictData;
import com.javaboot.system.service.ISysDictDataService;

/**
 * 数据字典信息
 *
 * @author javaboot
 */
@Controller
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
    private String prefix = "system/dict/data" ;

    @Autowired
    private ISysDictDataService dictDataService;

    @RequiresPermissions("system:dict:view")
    @GetMapping()
    public String dictData() {
        return prefix + "/data" ;
    }

    @PostMapping("/list")
    @RequiresPermissions("system:dict:list")
    @ResponseBody
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "字典数据" , businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    /**
     * 新增字典类型
     */
    @GetMapping("/add/{dictType}")
    public String add(@PathVariable("dictType") String dictType, ModelMap mmap) {
        mmap.put("dictType" , dictType);
        return prefix + "/add" ;
    }

    /**
     * 新增保存字典类型
     */
    @Log(title = "字典数据" , businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dict:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated SysDictData dict) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        dict.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改字典类型
     */
    @GetMapping("/edit/{dictCode}")
    public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap) {
        mmap.put("dict" , dictDataService.selectDictDataById(dictCode));
        return prefix + "/edit" ;
    }

    /**
     * 修改保存字典类型
     */
    @Log(title = "字典数据" , businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dict:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated SysDictData dict) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        dict.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(dictDataService.updateDictData(dict));
    }

    @Log(title = "字典数据" , businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dict:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        if(Global.isDemoEnabled()){
            return AjaxResult.error("演示模式，不允许操作!");
        }
        return toAjax(dictDataService.deleteDictDataByIds(ids));
    }
}

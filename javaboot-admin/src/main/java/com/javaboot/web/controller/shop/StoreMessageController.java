package com.javaboot.web.controller.shop;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.shop.domain.StoreMessage;
import com.javaboot.shop.service.IStoreMessageService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.system.domain.SysUser;

/**
 * 消息Controller
 *
 * @author javaboot
 * @date 2021-11-25
 */
@Controller
@RequestMapping("/shop/storeMessage")
public class StoreMessageController extends BaseController {
    private String prefix = "shop/storeMessage";

    @Autowired
    private IStoreMessageService storeMessageService;

    @RequiresPermissions("shop:storeMessage:view")
    @GetMapping()
    public String storeMessage() {
        return prefix + "/storeMessage";
    }

    /**
     * 查询消息列表
     */
//    @RequiresPermissions("shop:storeMessage:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreMessage storeMessage) {
        try
        {
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            if(!user.isAdmin()){
                if(storeMessage == null){
                    storeMessage = new StoreMessage();
                }
                storeMessage.setReceiveId(user.getUserId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        startPage();
        List<StoreMessage> list = storeMessageService.selectStoreMessageList(storeMessage);
        return getDataTable(list);
    }

    /**
     * 导出消息列表
     */
    @RequiresPermissions("shop:storeMessage:export")
    @Log(title = "消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreMessage storeMessage) {
        List<StoreMessage> list = storeMessageService.selectStoreMessageList(storeMessage);
        ExcelUtil<StoreMessage> util = new ExcelUtil<StoreMessage>(StoreMessage.class);
        return util.exportExcel(list, "storeMessage");
    }

    /**
     * 新增消息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存消息
     */
    @RequiresPermissions("shop:storeMessage:add")
    @Log(title = "消息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreMessage storeMessage) {
        return toAjax(storeMessageService.insertStoreMessage(storeMessage));
    }

    /**
     * 修改消息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreMessage storeMessage = storeMessageService.selectStoreMessageById(id);
        mmap.put("storeMessage", storeMessage);
        return prefix + "/edit";
    }

    /**
     * 修改保存消息
     */
    @RequiresPermissions("shop:storeMessage:edit")
    @Log(title = "消息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreMessage storeMessage) {
        return toAjax(storeMessageService.updateStoreMessage(storeMessage));
    }

    /**
     * 删除消息
     */
    @RequiresPermissions("shop:storeMessage:remove")
    @Log(title = "消息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeMessageService.deleteStoreMessageByIds(ids));
    }
}

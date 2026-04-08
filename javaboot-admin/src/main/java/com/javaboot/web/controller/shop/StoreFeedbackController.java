package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreFeedback;
import com.javaboot.shop.service.IStoreFeedbackService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 留言反馈
 *
 * @author javaboot
 * @date 2019-08-29
 */
@Controller
@RequestMapping("/shop/feedback")
public class StoreFeedbackController extends BaseController {
    private String prefix = "shop/feedback";

    @Autowired
    private IStoreFeedbackService feedbackService;

    @RequiresPermissions("shop:feedback:view")
    @GetMapping()
    public String feedback() {
        return prefix + "/feedback";
    }

    /**
     * 查询留言反馈列表
     */
    @RequiresPermissions("shop:feedback:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreFeedback storeFeedback) {
        startPage();
        List<StoreFeedback> list = feedbackService.selectStoreFeedbackList(storeFeedback);
        return getDataTable(list);
    }

    /**
     * 导出留言列表
     */
    @RequiresPermissions("shop:feedback:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreFeedback storeFeedback) {
        List<StoreFeedback> list = feedbackService.selectStoreFeedbackList(storeFeedback);
        ExcelUtil<StoreFeedback> util = new ExcelUtil<StoreFeedback>(StoreFeedback.class);
        return util.exportExcel(list, "feedback");
    }

    /**
     * 删除留言
     */
    @RequiresPermissions("shop:feedback:remove")
    @Log(title = "优惠券主", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(feedbackService.deleteStoreFeedbackByIds(ids));
    }
}

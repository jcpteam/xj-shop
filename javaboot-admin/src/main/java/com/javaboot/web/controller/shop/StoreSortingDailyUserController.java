package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.List;

import com.javaboot.common.core.text.Convert;
import com.javaboot.shop.service.IStoreSortingDailyWorkService;
import com.javaboot.system.vo.SortingDailyUserReq;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysUserService;
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
import com.javaboot.shop.domain.StoreSortingDailyUser;
import com.javaboot.shop.service.IStoreSortingDailyUserService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 分拣每日考勤员工关联Controller
 * 
 * @author javaboot
 * @date 2021-06-12
 */
@Controller
@RequestMapping("/shop/sortingDailyUser")
public class StoreSortingDailyUserController extends BaseController {
    private String prefix = "shop/sortingDailyUser";

    @Autowired
    private IStoreSortingDailyUserService storeSortingDailyUserService;


    @Autowired
    private IStoreSortingDailyWorkService storeSortingDailyWorkService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("shop:sortingDailyUser:view")
    @GetMapping()
    public String sortingDailyUser() {
        return prefix + "/sortingDailyUser";
    }

    /**
     * 查询分拣每日考勤员工关联列表
     */
//    @RequiresPermissions("shop:sortingDailyUser:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSortingDailyUser storeSortingDailyUser) {
        startPage();
        List<StoreSortingDailyUser> list = storeSortingDailyUserService.selectStoreSortingDailyUserList(storeSortingDailyUser);
        return getDataTable(list);
    }

//    @RequiresPermissions("shop:sortingDailyUser:list")
    @PostMapping("/list/{workId}")
    @ResponseBody
    public TableDataInfo listWithWrokId(@PathVariable("workId") Long workId) {
        startPage();
        StoreSortingDailyUser where = new StoreSortingDailyUser();
        where.setWorkId(workId);
        List<StoreSortingDailyUser> list = storeSortingDailyUserService.selectStoreSortingDailyUserList(where);
        return getDataTable(list);
    }

    /**
     * 导出分拣每日考勤员工关联列表
     */
    @RequiresPermissions("shop:sortingDailyUser:export")
    @Log(title = "分拣每日考勤员工关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSortingDailyUser storeSortingDailyUser) {
        List<StoreSortingDailyUser> list = storeSortingDailyUserService.selectStoreSortingDailyUserList(storeSortingDailyUser);
        ExcelUtil<StoreSortingDailyUser> util = new ExcelUtil<StoreSortingDailyUser>(StoreSortingDailyUser.class);
        return util.exportExcel(list, "sortingDailyUser");
    }

    /**
     * 新增分拣每日考勤员工关联
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存分拣每日考勤员工关联
     */
    @RequiresPermissions("shop:sortingDailyUser:add")
    @Log(title = "分拣每日考勤员工关联", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSortingDailyUser storeSortingDailyUser) {
        return toAjax(storeSortingDailyUserService.insertStoreSortingDailyUser(storeSortingDailyUser));
    }

    /**
     * 修改分拣每日考勤员工关联
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreSortingDailyUser storeSortingDailyUser = storeSortingDailyUserService.selectStoreSortingDailyUserById(id);
        mmap.put("storeSortingDailyUser", storeSortingDailyUser);
        return prefix + "/edit";
    }

    /**
     * 修改保存分拣每日考勤员工关联
     */
    @RequiresPermissions("shop:sortingDailyUser:edit")
    @Log(title = "分拣每日考勤员工关联", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSortingDailyUser storeSortingDailyUser) {
        return toAjax(storeSortingDailyUserService.updateStoreSortingDailyUser(storeSortingDailyUser));
    }

    /**
     * 删除分拣每日考勤员工关联
     */
    @RequiresPermissions("shop:sortingDailyUser:remove")
    @Log(title = "分拣每日考勤员工关联", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSortingDailyUserService.deleteStoreSortingDailyUserByIds(ids));
    }

    /**
     * 查询线路关联的商户详细
     */
//    @RequiresPermissions("shop:sortingDailyUser:view")
    @GetMapping("/detail/{workId}")
    public String detail(@PathVariable("workId") Long workId, ModelMap mmap) {
        //查询用户信息、及用户角色部门信息列表返回
        mmap.put("sortingDailyWork", storeSortingDailyWorkService.selectStoreSortingDailyWorkById(workId));
        StoreSortingDailyUser where = new StoreSortingDailyUser();
        where.setWorkId(workId);
        mmap.put("sortingDailyUserList", storeSortingDailyUserService.selectStoreSortingDailyUserList(where));
        return prefix + "/sortingDailyUser";
    }

    /**
     * 选择用户
     */
    @GetMapping("/authUser/selectUser/{workId}")
    public String selectUser(@PathVariable("workId") Long workId, ModelMap mmap) {
        mmap.put("sortingDailyWork" , storeSortingDailyWorkService.selectStoreSortingDailyWorkById(workId));
        return prefix + "/selectUser" ;
    }

    /**
     * 查询未分配用户角色列表
     */
    @RequiresPermissions("system:role:list")
    @PostMapping("/authUser/selectUnallocatedSortingWorkList")
    @ResponseBody
    public TableDataInfo selectUnallocatedSortingWorkList(SortingDailyUserReq user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedSortingWorkList(user);
        return getDataTable(list);
    }

    /**
     * 添加分拣员
     */
    @PostMapping("/authUser/addSortingUserList")
    @ResponseBody
    public AjaxResult addSortingUserList(Long workId, String userIds) {
        return toAjax(storeSortingDailyUserService.addSortingUserList(workId,userIds));
    }
}

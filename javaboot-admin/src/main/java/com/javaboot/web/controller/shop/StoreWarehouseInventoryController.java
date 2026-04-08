package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.shop.domain.StoreHouse;
import com.javaboot.shop.domain.StoreWarehouseInventory;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.dto.StoreWarehouseInventoryExportDTO;
import com.javaboot.shop.mapper.StoreWarehouseInventoryItemMapper;
import com.javaboot.shop.service.IStoreHouseService;
import com.javaboot.shop.service.IStoreWarehouseInventoryService;
import com.javaboot.shop.vo.StoreWarehouseInventoryVO;
import com.javaboot.system.domain.SysDept;
import com.javaboot.system.domain.SysUser;
import com.javaboot.system.service.ISysDeptService;
import com.javaboot.system.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品盘库Controller
 * 
 * @author lqh
 * @date 2021-05-23
 */
@Controller
@RequestMapping("/shop/inventory")
public class StoreWarehouseInventoryController extends BaseController {
    private String prefix = "shop/inventory";

    @Autowired
    private IStoreWarehouseInventoryService storeWarehouseInventoryService;
    @Autowired
    private IStoreHouseService storeHouseService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private StoreWarehouseInventoryItemMapper storeWarehouseInventoryItemMapper;

    @RequiresPermissions("shop:inventory:view")
    @GetMapping()
    public String inventory() {
        return prefix + "/inventory";
    }

    /**
     * 查询商品盘库列表
     */
    @RequiresPermissions("shop:inventory:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreWarehouseInventory storeWarehouseInventory) {
        startPage();
        if(StringUtils.isBlank(storeWarehouseInventory.getDeptId())) {
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            storeWarehouseInventory.setDeptId(user.isAdmin()?null:user.getDeptId());
        }

        List<StoreWarehouseInventoryVO> list = storeWarehouseInventoryService.selectStoreWarehouseInventoryList(storeWarehouseInventory);

        List<StoreHouse> houseList =  getHouseList();
        List<SysDept> deptList = getDeptList();
        if(CollectionUtils.isNotEmpty(list)) {
            List<String> uid = new ArrayList<>();
            list.forEach(o-> {
                // 设置仓库名称
                houseList.forEach(h-> {
                    if (h.getStoreId().equals(o.getWarehouseId()))
                    {
                        o.setWarehouseName(h.getStoreName());
                    }
                });
                // 设置部门名称
                deptList.forEach(d-> {
                    if (d.getDeptId().equals(o.getDeptId()))
                    {
                        o.setDeptName(d.getDeptName());
                    }
                });
                if(!uid.contains(o.getCreatorId()+"")) {
                    uid.add(o.getCreatorId()+"");
                }

            });
            List<SysUser> userList = sysUserService.selectUserByIds(uid);
            list.forEach(o-> {
                // 设置创建者名称
                userList.forEach(h-> {
                    if (h.getUserId().equals(o.getCreatorId()))
                    {
                        o.setCreatorIdName(h.getUserName());
                    }
                });
            });
        }
        // 获取区域名称
        return getDataTable(list);
    }

    /**
     * 导出商品盘库列表
     */
    @RequiresPermissions("shop:inventory:export")
    @Log(title = "商品盘库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        List<StoreWarehouseInventoryExportDTO> list = storeWarehouseInventoryItemMapper.selectExportInventoryItemList(storeWarehouseInventory);
        ExcelUtil<StoreWarehouseInventoryExportDTO> util = new ExcelUtil<>(StoreWarehouseInventoryExportDTO.class);
        return util.exportExcel(list, "盘库单");
    }

    /**
     * 新增商品盘库
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {

        mmap.put("houseList",getHouseList() );
        return prefix + "/add";
    }

    private List<StoreHouse> getHouseList(){
        StoreHouse storeHouse=new StoreHouse();
        storeHouse.setStatus(Constants.NORMAL);
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(!user.isAdmin()){
            storeHouse.setDeptId(user.getDeptId());
        }
        return storeHouseService.selectStoreHouseList(storeHouse);
    }

    private List<SysDept> getDeptList() {
        SysDept sysDept = new SysDept();
        sysDept.setDelFlag("0");
        sysDept.setStatus("0");
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(!user.isAdmin()){
            sysDept.setDeptId(user.getDeptId());
        }
        return sysDeptService.selectDeptList(sysDept);
    }
    /**
     * 新增保存商品盘库
     */
    @RequiresPermissions("shop:inventory:add")
    @Log(title = "商品盘库", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        return toAjax(storeWarehouseInventoryService.insertStoreWarehouseInventory(storeWarehouseInventory));
    }

    /**
     * 修改商品盘库
     */
    @GetMapping("/edit/{inventoryId}")
    public String edit(@PathVariable("inventoryId") Long inventoryId, ModelMap mmap) {
        StoreWarehouseInventory storeWarehouseInventory = storeWarehouseInventoryService.selectStoreWarehouseInventoryById(inventoryId);
        mmap.put("storeWarehouseInventory", storeWarehouseInventory);
        mmap.put("houseList",  getHouseList());
        return prefix + "/edit";
    }

    /**
     * 修改保存商品盘库
     */
    @RequiresPermissions("shop:inventory:edit")
    @Log(title = "商品盘库", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        return toAjax(storeWarehouseInventoryService.updateStoreWarehouseInventory(storeWarehouseInventory));
    }

    /**
     * 删除商品盘库
     */
    @RequiresPermissions("shop:inventory:remove")
    @Log(title = "商品盘库", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeWarehouseInventoryService.deleteStoreWarehouseInventoryByIds(ids));
    }

    /**
     * 根据选择的仓库获取盘库商品SPU列表
     */
    @PostMapping("/spus/init")
    @ResponseBody
    public List<StoreWarehouseInventoryItem> spuInit(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        return storeWarehouseInventoryService.calculationInventoryGoods(storeWarehouseInventory);
    }

    /**
     * 查询所有盘库商品SPU信息列表
     */
    @PostMapping("/spus/list")
    @ResponseBody
    public TableDataInfo spuList(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        if(!storeWarehouseInventory.getIsQueryAll()){
            startPage();
        }
        List<StoreWarehouseInventoryItem> list = storeWarehouseInventoryService.initInventorySpus(storeWarehouseInventory);
        return getDataTable(list);
    }


    /**
     * 查询所有盘库商品SPU信息列表
     */
    @PostMapping("/latestdate")
    @ResponseBody
    public StoreWarehouseInventoryDTO lastInventoryDate(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        StoreWarehouseInventoryDTO dto = new StoreWarehouseInventoryDTO();
        dto.setStartTime(storeWarehouseInventoryService.lastInventoryDate(storeWarehouseInventory.getWarehouseId()));
        return dto;
    }
}

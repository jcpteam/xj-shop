package com.javaboot.web.controller.shop;

import com.javaboot.common.annotation.Log;
import com.javaboot.common.constant.Constants;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.core.page.TableDataInfo;
import com.javaboot.common.core.text.Convert;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.common.exception.BusinessException;
import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import com.javaboot.shop.domain.StoreGoodsSalesUnit;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsQueryDTO;
import com.javaboot.shop.dto.StoreWarehouseInventoryDTO;
import com.javaboot.shop.mapper.StoreWarehouseInventoryItemMapper;
import com.javaboot.shop.service.IStoreGoodsQuotationGoodsService;
import com.javaboot.shop.service.IStoreGoodsSalesUnitService;
import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;
import com.javaboot.shop.vo.StoreGoodsSpuVO;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 商品列表Controller
 *
 * @author lqh
 * @date 2021-05-23
 */
@Controller
@RequestMapping("/shop/quotationGoods")
public class StoreGoodsQuotationGoodsController extends BaseController {
    private String prefix = "shop/quotationGoods";

    @Autowired
    private IStoreGoodsQuotationGoodsService storeGoodsQuotationGoodsService;

    @Autowired
    private IStoreGoodsSalesUnitService storeGoodsSalesUnitService;

    @Autowired
    private StoreWarehouseInventoryItemMapper storeWarehouseInventoryItemMapper;

    @RequiresPermissions("shop:goods:view")
    @GetMapping()
    public String goods(HttpServletRequest request, ModelMap mmap) {
        String quotationId = request.getParameter("quotationId");
        if (StringUtils.isNotEmpty(quotationId)) {
            mmap.put("quotationId", quotationId);
        }
        return prefix + "/goods";
    }

    /**
     * 查询商品列表列表
     */
    @RequiresPermissions("shop:goods:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreGoodsQuotationGoodsQueryDTO storeGoodsQuotationGoods) {
        if(!storeGoodsQuotationGoods.isQueryAll()){
            startPage();
        }
        if(StringUtils.isEmpty(storeGoodsQuotationGoods.getDeptId())){
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            storeGoodsQuotationGoods.setLoginUserDeptId(user.isAdmin()?null:user.getDeptId());
        }
        if(StringUtils.isNotEmpty(storeGoodsQuotationGoods.getGoodsIds())){
            storeGoodsQuotationGoods.setGoodsIdList(Arrays.asList((Long[])ConvertUtils.convert(storeGoodsQuotationGoods.getGoodsIds().split(","), Long.class)));
        }
        List<StoreGoodsQuotationGoodsVO> list = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsList(storeGoodsQuotationGoods);
        return getDataTable(list);
    }

    /**
     * 查询商品列表列表
     */
    @PostMapping("/appList")
    @ResponseBody
    public TableDataInfo appList(StoreGoodsQuotationGoodsQueryDTO storeGoodsQuotationGoods) {
        if(!storeGoodsQuotationGoods.isQueryAll()){
            startPage();
        }
        List<StoreGoodsQuotationGoodsVO> list = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsList(storeGoodsQuotationGoods);
        return getDataTable(list);
    }


    /**
     * 新增商品列表
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request, ModelMap mmap) {
        String quotationId = request.getParameter("quotationId");
        if (StringUtils.isNotEmpty(quotationId)) {
            mmap.put("quotationId", quotationId);
        }
        mmap.put("unitIdList", getSpecificationsList());
        return prefix + "/add";
    }

    /**
     * 新增保存商品列表
     */
    @RequiresPermissions("shop:goods:add")
    @Log(title = "商品列表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreGoodsQuotationGoods storeGoodsQuotationGoods) {
        try {
            return toAjax(storeGoodsQuotationGoodsService.insertStoreGoodsQuotationGoods(storeGoodsQuotationGoods));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改商品列表
     */
    @GetMapping("/edit/{goodsId}")
    public String edit(@PathVariable("goodsId") Long goodsId, ModelMap mmap) {
        StoreGoodsQuotationGoodsVO storeGoodsQuotationGoods = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsById(goodsId);
        mmap.put("storeGoodsQuotationGoods", storeGoodsQuotationGoods);
        mmap.put("unitIdList", getSpecificationsList());
        mmap.put("unitPriceList", storeGoodsQuotationGoods.getUnitPriceList());
        return prefix + "/edit";
    }

    /**
     * 获取正常规格
     *
     * @return
     */
    private List<StoreGoodsSalesUnit> getSpecificationsList() {
        StoreGoodsSalesUnit salesUnit = new StoreGoodsSalesUnit();
        salesUnit.setStatus(Constants.NORMAL);
        List<StoreGoodsSalesUnit> list = storeGoodsSalesUnitService.selectStoreGoodsSalesUnitList(salesUnit);
        if (CollectionUtils.isNotEmpty(list)) {
            list.sort(Comparator.comparing(StoreGoodsSalesUnit::getOrderNum));
        }
        return list;
    }

    /**
     * 修改保存商品列表
     */
    @RequiresPermissions("shop:goods:edit")
    @Log(title = "修改商品列表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreGoodsQuotationGoods storeGoodsQuotationGoods) {
        try {
            return toAjax(storeGoodsQuotationGoodsService.updateStoreGoodsQuotationGoods(storeGoodsQuotationGoods));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改保存商品列表
     */
    @RequiresPermissions("shop:goods:edit")
    @Log(title = "批量上下架", businessType = BusinessType.UPDATE)
    @PostMapping("/batchEdit")
    @ResponseBody
    public AjaxResult batchEditSave(StoreGoodsQuotationGoodsQueryDTO dto) {
        try {
            List<String> ids = Arrays.asList(Convert.toStrArray(dto.getGoodsIds()));
            List<StoreGoodsQuotationGoods> goodsQuotationGoodsList = new ArrayList<>();
            ids.forEach(id->{
                StoreGoodsQuotationGoods goodsQuotationGoods = new StoreGoodsQuotationGoods();
                goodsQuotationGoods.setGoodsId(Long.parseLong(id));
                goodsQuotationGoods.setStatus(dto.getStatus());
                goodsQuotationGoodsList.add(goodsQuotationGoods);
            });
            return toAjax(storeGoodsQuotationGoodsService.saveOrUpdateBatch(goodsQuotationGoodsList));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除商品列表
     */
    @RequiresPermissions("shop:goods:remove")
    @Log(title = "商品列表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(storeGoodsQuotationGoodsService.deleteStoreGoodsQuotationGoodsByIds(ids));
        } catch (BusinessException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改商品列表
     */
    @RequiresPermissions("shop:goods:view")
    @GetMapping("/view/{goodsId}")
    public String view(@PathVariable("goodsId") Long goodsId, ModelMap mmap) {
        StoreGoodsQuotationGoodsVO storeGoodsQuotationGoods = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsById(goodsId);
        mmap.put("storeGoodsQuotationGoods", storeGoodsQuotationGoods);
        mmap.put("unitIdList", getSpecificationsList());
        return prefix + "/view";
    }

    /**
     * 获取单位换算关系
     */
    @RequiresPermissions("shop:goods:view")
    @PostMapping("/unit/conversion")
    @ResponseBody
    public Double view(StoreWarehouseInventoryDTO storeWarehouseInventory) {
        if(StringUtils.isEmpty(storeWarehouseInventory.getDeptId())) {
            storeWarehouseInventory.setDeptId("0");
        }
        List<StoreGoodsSpuVO> conversionList = storeWarehouseInventoryItemMapper.selectSpuUnitConversion(storeWarehouseInventory);
        if(CollectionUtils.isNotEmpty(conversionList)) {
            return conversionList.get(0).getSubCaseMain();
        }
        return 1.0;
    }
}

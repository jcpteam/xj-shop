package com.javaboot.web.controller.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javaboot.common.utils.StringUtils;
import com.javaboot.shop.domain.StoreGoodsSpu;
import com.javaboot.shop.service.IStoreGoodsSpuService;
import com.javaboot.shop.vo.StoreGoodsSpuVO;
import org.apache.commons.collections.CollectionUtils;
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
import com.javaboot.shop.domain.StoreSortingPerformanceStand;
import com.javaboot.shop.service.IStoreSortingPerformanceStandService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 分拣绩效标准信息Controller
 * 
 * @author javaboot
 * @date 2021-07-01
 */
@Controller
@RequestMapping("/shop/performanceStand")
public class StoreSortingPerformanceStandController extends BaseController {
    private String prefix = "shop/performanceStand";

    @Autowired
    private IStoreSortingPerformanceStandService storeSortingPerformanceStandService;

    @Autowired
    IStoreGoodsSpuService storeGoodsSpuService;

    @RequiresPermissions("shop:performanceStand:view")
    @GetMapping()
    public String performanceStand() {
        return prefix + "/performanceStand";
    }

    /**
     * 查询分拣绩效标准信息列表
     */
    @RequiresPermissions("shop:performanceStand:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(StoreSortingPerformanceStand storeSortingPerformanceStand) {
        startPage();
        List<StoreSortingPerformanceStand> list = storeSortingPerformanceStandService.selectStoreSortingPerformanceStandList(storeSortingPerformanceStand);

        //如果当前没有分拣绩效标准数据，则按照spu自动生成一把
        if(CollectionUtils.isEmpty(list) && StringUtils.isEmpty(storeSortingPerformanceStand.getCustomerType()) && StringUtils.isEmpty(storeSortingPerformanceStand.getSpuName())){
            //再查询一下全部数据
            list = storeSortingPerformanceStandService.selectStoreSortingPerformanceStandList(null);
            if(CollectionUtils.isEmpty(list)){
                //先查询spu商品数据
                List<StoreGoodsSpuVO> spuList = storeGoodsSpuService.selectStoreGoodsSpuList(null);
                if(CollectionUtils.isNotEmpty(spuList)){
                    for (StoreGoodsSpuVO info : spuList){
                        StoreSortingPerformanceStand insert1 = new StoreSortingPerformanceStand();
                        insert1.setCustomerType("大客户");
                        insert1.setSpuNo(info.getSpuNo());
                        insert1.setPerformUnitId(2L); //只
                        insert1.setGoodUnitId(1L);
                        insert1.setSortingWeightRatio(0d);
                        storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(insert1);

                        StoreSortingPerformanceStand insert2 = new StoreSortingPerformanceStand();
                        insert2.setCustomerType("批发客户");
                        insert2.setSpuNo(info.getSpuNo());
                        insert2.setPerformUnitId(2L); //只
                        insert2.setGoodUnitId(1L);
                        insert2.setSortingWeightRatio(0d);
                        storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(insert2);

                        StoreSortingPerformanceStand insert3 = new StoreSortingPerformanceStand();
                        insert3.setCustomerType("零散客户");
                        insert3.setSpuNo(info.getSpuNo());
                        insert3.setPerformUnitId(2L); //只
                        insert3.setGoodUnitId(1L);
                        insert3.setSortingWeightRatio(0d);
                        storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(insert3);

                        StoreSortingPerformanceStand insert4 = new StoreSortingPerformanceStand();
                        insert4.setCustomerType("平台客户");
                        insert4.setSpuNo(info.getSpuNo());
                        insert4.setPerformUnitId(3L); //盒
                        insert4.setGoodUnitId(1L);
                        insert4.setSortingWeightRatio(0d);
                        storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(insert4);
                    }
                }
            }
            list = storeSortingPerformanceStandService.selectStoreSortingPerformanceStandList(storeSortingPerformanceStand);
        }
        return getDataTable(list);
    }

    @PostMapping("/syncStandData")
    @ResponseBody
    public AjaxResult syncStandData() {
        //同步数据
        //1、先查询已有的spu数据
        List<StoreSortingPerformanceStand> existList = storeSortingPerformanceStandService.selectStoreSortingPerformanceStandList(null);
        Map<String,StoreSortingPerformanceStand> spuNo2StandInfoMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(existList)){
            for (StoreSortingPerformanceStand stand : existList){
                spuNo2StandInfoMap.put(stand.getSpuNo(),stand);
            }
        }

        //2、查询spu商品数据
        List<StoreGoodsSpuVO> spuList = storeGoodsSpuService.selectStoreGoodsSpuList(null);
        if(CollectionUtils.isNotEmpty(spuList)){
            for (StoreGoodsSpuVO info : spuList){
                if(spuNo2StandInfoMap.get(info.getSpuNo()) == null){
                    StoreSortingPerformanceStand insert1 = new StoreSortingPerformanceStand();
                    insert1.setCustomerType("大客户");
                    insert1.setSpuNo(info.getSpuNo());
                    insert1.setPerformUnitId(3L); //盒
                    insert1.setGoodUnitId(1L);
                    storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(insert1);

                    StoreSortingPerformanceStand insert2 = new StoreSortingPerformanceStand();
                    insert2.setCustomerType("批发客户");
                    insert2.setSpuNo(info.getSpuNo());
                    insert2.setPerformUnitId(2L); //只
                    insert2.setGoodUnitId(1L);
                    storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(insert2);

                    StoreSortingPerformanceStand insert3 = new StoreSortingPerformanceStand();
                    insert3.setCustomerType("零散客户");
                    insert3.setSpuNo(info.getSpuNo());
                    insert3.setPerformUnitId(1L); //公斤
                    insert3.setGoodUnitId(1L);
                    storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(insert3);

                    StoreSortingPerformanceStand insert4 = new StoreSortingPerformanceStand();
                    insert4.setCustomerType("平台客户");
                    insert4.setSpuNo(info.getSpuNo());
                    insert4.setPerformUnitId(1L); //公斤
                    insert4.setGoodUnitId(1L);
                    storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(insert4);
                }
            }
        }
        return AjaxResult.success();
    }

    /**
     * 导出分拣绩效标准信息列表
     */
    @RequiresPermissions("shop:performanceStand:export")
    @Log(title = "分拣绩效标准信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreSortingPerformanceStand storeSortingPerformanceStand) {
        List<StoreSortingPerformanceStand> list = storeSortingPerformanceStandService.selectStoreSortingPerformanceStandList(storeSortingPerformanceStand);
        ExcelUtil<StoreSortingPerformanceStand> util = new ExcelUtil<StoreSortingPerformanceStand>(StoreSortingPerformanceStand.class);
        return util.exportExcel(list, "performanceStand");
    }

    /**
     * 新增分拣绩效标准信息
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存分拣绩效标准信息
     */
    @RequiresPermissions("shop:performanceStand:add")
    @Log(title = "分拣绩效标准信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(StoreSortingPerformanceStand storeSortingPerformanceStand) {
        return toAjax(storeSortingPerformanceStandService.insertStoreSortingPerformanceStand(storeSortingPerformanceStand));
    }

    /**
     * 修改分拣绩效标准信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        StoreSortingPerformanceStand storeSortingPerformanceStand = storeSortingPerformanceStandService.selectStoreSortingPerformanceStandById(id);
        mmap.put("storeSortingPerformanceStand", storeSortingPerformanceStand);
        return prefix + "/edit";
    }

    /**
     * 修改保存分拣绩效标准信息
     */
    @RequiresPermissions("shop:performanceStand:edit")
    @Log(title = "分拣绩效标准信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(StoreSortingPerformanceStand storeSortingPerformanceStand) {
        return toAjax(storeSortingPerformanceStandService.updateStoreSortingPerformanceStand(storeSortingPerformanceStand));
    }

    /**
     * 删除分拣绩效标准信息
     */
    @RequiresPermissions("shop:performanceStand:remove")
    @Log(title = "分拣绩效标准信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeSortingPerformanceStandService.deleteStoreSortingPerformanceStandByIds(ids));
    }
}

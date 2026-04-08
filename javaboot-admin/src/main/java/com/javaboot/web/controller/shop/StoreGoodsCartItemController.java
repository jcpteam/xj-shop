package com.javaboot.web.controller.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.javaboot.shop.domain.StoreGoodsQuotationGoods;
import com.javaboot.shop.dto.StoreGoodsQuotationGoodsQueryDTO;
import com.javaboot.shop.service.IStoreGoodsQuotationGoodsService;
import com.javaboot.shop.vo.StoreGoodsQuotationGoodsVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.javaboot.common.annotation.Log;
import com.javaboot.common.enums.BusinessType;
import com.javaboot.shop.domain.StoreGoodsCartItem;
import com.javaboot.shop.service.IStoreGoodsCartItemService;
import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.core.domain.AjaxResult;
import com.javaboot.common.utils.poi.ExcelUtil;
import com.javaboot.common.core.page.TableDataInfo;

/**
 * 购物车明细Controller
 *
 * @author javaboot
 * @date 2021-08-04
 */
@Controller
@RequestMapping("/shop/cartItem")
public class StoreGoodsCartItemController extends BaseController {
    private String prefix = "shop/cartItem";

    @Autowired
    private IStoreGoodsCartItemService storeGoodsCartItemService;

    @Autowired
    private IStoreGoodsQuotationGoodsService storeGoodsQuotationGoodsService;

    @RequiresPermissions("shop:cartItem:view")
    @GetMapping()
    public String cartItem() {
        return prefix + "/cartItem";
    }

    /**
     * 查询购物车明细列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestBody StoreGoodsCartItem storeGoodsCartItem) {
//        startPage();
        List<StoreGoodsCartItem> list = storeGoodsCartItemService.selectStoreGoodsCartItemList(storeGoodsCartItem);
        //查询购物车商品的最新价格信息
        if(CollectionUtils.isNotEmpty(list)){
            List<Long> goodsIds = new ArrayList<>();
            for (StoreGoodsCartItem s : list){
                try {
                    Long id = Long.valueOf(s.getGoodsId());
                    if(!goodsIds.contains(id)){
                        goodsIds.add(id);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            StoreGoodsQuotationGoodsQueryDTO whereGoods = new StoreGoodsQuotationGoodsQueryDTO();
            whereGoods.setGoodsIdList(goodsIds);
            List<StoreGoodsQuotationGoodsVO> storeGoodsQuotationGoodsVOList = storeGoodsQuotationGoodsService.selectStoreGoodsQuotationGoodsList(whereGoods);
            Map<Long,StoreGoodsQuotationGoodsVO> goodsId2InfoMap = storeGoodsQuotationGoodsVOList.stream().collect(Collectors.toMap(StoreGoodsQuotationGoodsVO::getGoodsId,s->s));
            if(CollectionUtils.isNotEmpty(storeGoodsQuotationGoodsVOList) && goodsId2InfoMap != null && goodsId2InfoMap.size() > 0){
                for (StoreGoodsCartItem s : list){
                    try {
                        Long id = Long.valueOf(s.getGoodsId());
                        s.setStoreGoodsQuotationGoods(goodsId2InfoMap.get(id));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return getDataTable(list);
    }

    /**
     * 导出购物车明细列表
     */
    @RequiresPermissions("shop:cartItem:export")
    @Log(title = "购物车明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(StoreGoodsCartItem storeGoodsCartItem) {
        List<StoreGoodsCartItem> list = storeGoodsCartItemService.selectStoreGoodsCartItemList(storeGoodsCartItem);
        ExcelUtil<StoreGoodsCartItem> util = new ExcelUtil<StoreGoodsCartItem>(StoreGoodsCartItem.class);
        return util.exportExcel(list, "cartItem");
    }

    /**
     * 新增购物车明细
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存购物车明细
     */
    @Log(title = "购物车明细", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody StoreGoodsCartItem storeGoodsCartItem) {
        if (storeGoodsCartItem.getItemId() != null) {
            //如果数量小于0，则删除改订单商品
            if (storeGoodsCartItem.getQuantity() <= 0) {
                storeGoodsCartItem.setStatus("0");
            }
            AjaxResult ret = editSave(storeGoodsCartItem);
            if (ret.isSuccess()) {
                return AjaxResult.success(storeGoodsCartItem);
            } else {
                return AjaxResult.error();
            }
        } else {
            int ret = storeGoodsCartItemService.insertStoreGoodsCartItem(storeGoodsCartItem);
            if (ret > 0) {
                StoreGoodsCartItem where = new StoreGoodsCartItem();
                where.setUserId(storeGoodsCartItem.getUserId());
                where.setGoodsId(storeGoodsCartItem.getGoodsId());
                where.setStatus("1");
                List<StoreGoodsCartItem> existList = storeGoodsCartItemService.selectStoreGoodsCartItemList(where);
                //正常情况下只有一条记录
                if (CollectionUtils.isNotEmpty(existList)) {
                    return AjaxResult.success(existList.get(0));
                } else {
                    return AjaxResult.success(storeGoodsCartItem);
                }
            } else {
                return AjaxResult.error();
            }
        }
    }

    /**
     * 修改购物车明细
     */
    @GetMapping("/edit/{itemId}")
    public String edit(@PathVariable("itemId") Long itemId, ModelMap mmap) {
        StoreGoodsCartItem storeGoodsCartItem = storeGoodsCartItemService.selectStoreGoodsCartItemById(itemId);
        mmap.put("storeGoodsCartItem", storeGoodsCartItem);
        return prefix + "/edit";
    }

    /**
     * 修改保存购物车明细
     */
    @Log(title = "购物车明细", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody StoreGoodsCartItem storeGoodsCartItem) {
        return toAjax(storeGoodsCartItemService.updateStoreGoodsCartItem(storeGoodsCartItem));
    }

    /**
     * 删除购物车明细
     */
    @RequiresPermissions("shop:cartItem:remove")
    @Log(title = "购物车明细", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(storeGoodsCartItemService.deleteStoreGoodsCartItemByIds(ids));
    }

    /**
     * 删除购物车明细
     */
    @Log(title = "删除购物车商品", businessType = BusinessType.DELETE)
    @PostMapping("/delCartGoodsWithUserId")
    @ResponseBody
    public AjaxResult delCartGoodsWithUserId(String userId) {
        StoreGoodsCartItem update = new StoreGoodsCartItem();
        update.setUserId(userId);
        update.setStatus("0");
        return toAjax(storeGoodsCartItemService.delCartGoodsWithUserId(update));
    }
}

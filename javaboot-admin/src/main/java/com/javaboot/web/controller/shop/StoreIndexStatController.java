package com.javaboot.web.controller.shop;

import com.javaboot.common.core.controller.BaseController;
import com.javaboot.common.utils.DateUtils;
import com.javaboot.shop.domain.StoreGoodsOrderItem;
import com.javaboot.shop.domain.StoreMember;
import com.javaboot.shop.domain.StoreWarehouseInventoryItem;
import com.javaboot.shop.domain.StoreWarehouseStockItem;
import com.javaboot.shop.dto.StoreStatDTO;
import com.javaboot.shop.mapper.StoreStatMapper;
import com.javaboot.shop.mapper.StoreWarehouseInventoryItemMapper;
import com.javaboot.shop.mapper.StoreWarehouseStockItemMapper;
import com.javaboot.shop.vo.StoreGoodsOrderVO;
import com.javaboot.system.domain.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *  首页统计
 */
@Controller
@RequestMapping("/shop/index/stat")
public class StoreIndexStatController extends BaseController
{

    @Autowired
    private StoreStatMapper storeStatMapper;

    @Autowired
    private StoreWarehouseInventoryItemMapper storeWarehouseInventoryItemMapper;

    @Autowired
    private StoreWarehouseStockItemMapper storeWarehouseStockItemMapper;



    @GetMapping("/bigScreen")
    public String bigScreen() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        String deptId=user.getDeptId();
        return redirect("http://47.92.241.101:37799/webroot/decision/link/osrp?dept_id="+deptId);
    }

    @GetMapping("/order/num")
    @ResponseBody
    public Integer orderNum() {
        return storeStatMapper.selectOrderTotalNum(initStoreStat());
    }

    @GetMapping("/merchant/num")
    @ResponseBody
    public Integer merchantNum() {
        return storeStatMapper.selectMerchantTotalNum(initStoreStat());
    }


    @GetMapping("/spu/num")
    @ResponseBody
    public Integer spuNum() {
        return storeStatMapper.selectSpuTotalNum(initStoreStat());
    }

    @GetMapping("/goods/num")
    @ResponseBody
    public Integer goodsNum() {
        return storeStatMapper.selectGoodsTotalNum(initStoreStat());
    }

    @GetMapping("/todayOrder/list")
    @ResponseBody
    public List<StoreGoodsOrderItem> todayOrder() {
        List<StoreGoodsOrderItem> list = storeStatMapper.selectTodayOrderList(initStoreStat());
        initName(list);
        return list;
    }

    @GetMapping("/noOrderMember/list")
    @ResponseBody
    public List<StoreMember> noOrderMember() {
        return storeStatMapper.selectNoOrderMerberList(initStoreStat());
    }

    @GetMapping("/order/total")
    @ResponseBody
    public List<StoreGoodsOrderVO> orderTotalList() {
        return storeStatMapper.selectOrderTotalList(initStoreStat());
    }

    @GetMapping("/nopay/list")
    @ResponseBody
    public List<StoreGoodsOrderVO> noPay() {
        return storeStatMapper.selectNoPayList(initStoreStat());
    }

    @GetMapping("/month/ordernum")
    @ResponseBody
    public List<StoreGoodsOrderItem> monthOrderNum() {
        StoreStatDTO dto = initStoreStat();
        // 月开始和结束时间
        dto.setStartTime(DateUtils.getMonthFirstDay().concat(" 09:00:00"));
        dto.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(new Date(), 1)).concat(" 09:00:00"));
        List<StoreGoodsOrderItem> list = storeStatMapper.selectSpuOrderNumsList(dto);
        initName(list);
        return list;
    }

    @GetMapping("/week/salenum")
    @ResponseBody
    public List<StoreGoodsOrderItem> weekSaleNum() {
        StoreStatDTO dto = initStoreStat();
        // 周开始和结束时间
        dto.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(DateUtils.parseDate(dto.getStartTime()), -7)));
        dto.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(DateUtils.parseDate(dto.getEndTime()), -1)));
        List<StoreGoodsOrderItem> list = storeStatMapper.selectSpuOrderNumsList(dto);
        initName(list);
        return list;
    }

    @GetMapping("/week/purchase")
    @ResponseBody
    public List<StoreGoodsOrderItem> weekPurchase() {
        StoreStatDTO dto = initStoreStat();
        // 周开始和结束时间
        dto.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(DateUtils.parseDate(dto.getStartTime()), -7)));
        dto.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(DateUtils.parseDate(dto.getEndTime()), -1)));

        StoreWarehouseStockItem stockItem = new StoreWarehouseStockItem();
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        stockItem.setDeptId(user.isAdmin() ? null : user.getDeptId());
        // 周开始和结束时间
        stockItem.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(DateUtils.parseDate(dto.getStartTime()), -7)));
        stockItem.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(DateUtils.parseDate(dto.getEndTime()), -1)));
        List<StoreWarehouseStockItem> stockList = storeWarehouseStockItemMapper.selectWareStockQuantity(stockItem);

        List<StoreGoodsOrderItem> list = storeStatMapper.selectSpuOrderNumsList(dto);
        initName(list);
        initStockNum(list, stockList);
        return list;
    }

    private StoreStatDTO initStoreStat() {
        StoreStatDTO dto = new StoreStatDTO();
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        dto.setDeptId(user.isAdmin() ? null : user.getDeptId());
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        Date today = new Date();
        if(hour >= 9 ) {
            dto.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, today).concat(" 09:00:00"));
            dto.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(today, 1)).concat(" 09:00:00"));
        } else {
            dto.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(today, -1)).concat(" 09:00:00"));
            dto.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, today).concat(" 09:00:00"));
        }

        // 周开始和结束时间
        dto.setWeekStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(DateUtils.parseDate(dto.getStartTime()), -7)));
        dto.setWeekEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.addDays(DateUtils.parseDate(dto.getEndTime()), -1)));
        return dto;
    }


    private void initName(List<StoreGoodsOrderItem> itemList) {
        List<StoreWarehouseInventoryItem> spuList = storeWarehouseInventoryItemMapper.selectAllInventorySpus(null);
        if(CollectionUtils.isNotEmpty(itemList) && CollectionUtils.isNotEmpty(spuList)) {

            Map<String, StoreWarehouseInventoryItem> spuMap = spuList.stream().collect(Collectors.toMap(StoreWarehouseInventoryItem::getSpuNo, Function
                .identity()));
            itemList.forEach(item -> {
                StoreWarehouseInventoryItem nameItem = spuMap.get(item.getSpuNo());
                if(nameItem != null) {
                    item.setUnitName(nameItem.getSubUnitName());
                    item.setSpuName(nameItem.getSpuName());
                }
            });
        }
    }


    private void initStockNum(List<StoreGoodsOrderItem> itemList, List<StoreWarehouseStockItem> stockList) {
        if(CollectionUtils.isNotEmpty(itemList) && CollectionUtils.isNotEmpty(stockList)) {

            Map<String, StoreWarehouseStockItem> spuMap = stockList.stream().collect(Collectors.toMap(StoreWarehouseStockItem::getSpuNo, Function
                .identity()));
            itemList.forEach(item -> {
                item.setQuantity(0.0);
                StoreWarehouseStockItem stockItem = spuMap.get(item.getSpuNo());
                if(stockItem != null)
                    item.setQuantity(stockItem.getQuantity());
            });
        }
    }

}
